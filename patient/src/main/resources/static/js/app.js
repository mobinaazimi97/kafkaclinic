let currentLang = 'fa';

function setLanguage(lang) {
    currentLang = lang;
    applyTranslations(lang);
}

document.addEventListener('DOMContentLoaded', () => {
    loadSpecializations();
    setLanguage(currentLang);
});

const specializationSelect = document.getElementById('specializationSelect');
const scheduleSelect = document.getElementById('scheduleSelect');
const appointmentForm = document.getElementById('appointmentForm');
const messageDiv = document.getElementById('message');

function loadSpecializations() {
    fetch('/patients/appointments/specializations')
        .then(res => res.ok ? res.json() : Promise.reject(res.statusText))
        .then(data => {
            specializationSelect.innerHTML = `<option value="">${translations['select_specialization'] || 'انتخاب تخصص'}</option>`;
            data.forEach(spec => {
                specializationSelect.innerHTML += `<option value="${spec.specializationUuid}">${spec.skillName} - دکتر ${spec.doctorFirstName} ${spec.doctorLastName}</option>`;
            });
        })
        .catch(err => {
            messageDiv.innerHTML = `<div class="alert alert-danger">خطا در بارگذاری تخصص‌ها: ${err}</div>`;
        });
}

specializationSelect.addEventListener('change', () => {
    const specId = specializationSelect.value;
    scheduleSelect.disabled = true;
    scheduleSelect.innerHTML = `<option value="">${translations['loading'] || 'در حال بارگذاری...'}</option>`;
    if (!specId) {
        scheduleSelect.innerHTML = `<option value="">${translations['select_specialization'] || 'ابتدا تخصص را انتخاب کنید'}</option>`;
        return;
    }

    fetch(`/patients/appointments/specializations/${specId}`)
        .then(res => res.ok ? res.json() : Promise.reject(res.statusText))
        .then(data => {
            scheduleSelect.innerHTML = `<option value="">${translations['select_schedule'] || 'انتخاب زمان نوبت'}</option>`;
            if (data.length === 0) {
                scheduleSelect.innerHTML = `<option value="">زمان آزاد وجود ندارد</option>`;
            } else {
                data.forEach(schedule => {
                    const start = new Date(schedule.startDateTime);
                    const end = new Date(schedule.endDateTime);
                    const text = `${start.toLocaleDateString(currentLang === 'fa' ? 'fa-IR' : 'en-US')} ${start.toLocaleTimeString(currentLang === 'fa' ? 'fa-IR' : 'en-US', { hour: '2-digit', minute: '2-digit' })} - ${end.toLocaleTimeString(currentLang === 'fa' ? 'fa-IR' : 'en-US', { hour: '2-digit', minute: '2-digit' })}`;
                    scheduleSelect.innerHTML += `<option value="${schedule.scheduleUuid}">${text}</option>`;
                });
                scheduleSelect.disabled = false;
            }
        })
        .catch(err => {
            scheduleSelect.innerHTML = `<option value="">خطا در بارگذاری زمان‌ها</option>`;
        });
});

appointmentForm.addEventListener('submit', e => {
    e.preventDefault();
    messageDiv.innerHTML = '';

    const dto = {
        patientId: document.getElementById('patientIdInput').value.trim(),
        scheduleId: scheduleSelect.value,
        notes: document.getElementById('notesInput').value.trim()
    };

    if (!dto.patientId || !dto.scheduleId) {
        messageDiv.innerHTML = `<div class="alert alert-warning">لطفا تمام فیلدهای الزامی را پر کنید.</div>`;
        return;
    }

    fetch('/patients/appointments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dto)
    })
        .then(res => {
            if (!res.ok) return res.text().then(text => Promise.reject(text));
            return res.json();
        })
        .then(data => {
            messageDiv.innerHTML = `<div class="alert alert-success">نوبت با موفقیت ثبت شد.</div>`;
            appointmentForm.reset();
            scheduleSelect.disabled = true;
            scheduleSelect.innerHTML = `<option value="">ابتدا تخصص را انتخاب کنید</option>`;
        })
        .catch(err => {
            messageDiv.innerHTML = `<div class="alert alert-danger">خطا در ثبت نوبت: ${err}</div>`;
        });
});
