import http from './http'

export const scheduleApi = {
  getCoachSchedules(startDate) {
    const params = startDate ? `?startDate=${startDate}` : ''
    return http.get(`/api/schedule/coach${params}`)
  },

  createSchedule(data) {
    return http.post('/api/schedule', data)
  },

  updateSchedule(data) {
    return http.put('/api/schedule', data)
  },

  deleteSchedule(id) {
    return http.delete(`/api/schedule/${id}`)
  },

  toggleStatus(id) {
    return http.put(`/api/schedule/toggle/${id}`)
  },

  getAvailableSlots(date, coachId) {
    let params = `date=${date}`
    if (coachId) params += `&coachId=${coachId}`
    return http.get(`/api/schedule/available?${params}`)
  }
}

export const appointmentApi = {
  createAppointment(data) {
    return http.post('/api/appointment', data)
  },

  cancelAppointment(id) {
    return http.delete(`/api/appointment/${id}`)
  },

  confirmAppointment(id) {
    return http.put(`/api/appointment/confirm/${id}`)
  },

  rejectAppointment(id) {
    return http.put(`/api/appointment/reject/${id}`)
  },

  completeAppointment(id) {
    return http.put(`/api/appointment/complete/${id}`)
  },

  markNoShow(id) {
    return http.put(`/api/appointment/noshow/${id}`)
  },

  getStudentAppointments() {
    return http.get('/api/appointment/student')
  },

  getCoachAppointments(status) {
    const params = status != null ? `?status=${status}` : ''
    return http.get(`/api/appointment/coach${params}`)
  },

  getActiveAppointments() {
    return http.get('/api/appointment/active')
  },

  getProfile() {
    return http.get('/api/user/student/profile')
  }
}

export const coachApi = {
  getAll() {
    return http.get('/api/coach/all')
  }
}

export const vehicleApi = {
  getList(keyword, page, size) {
    const params = new URLSearchParams()
    if (keyword) params.append('keyword', keyword)
    params.append('page', page || 1)
    params.append('size', size || 100)
    return http.get(`/api/vehicle/list?${params}`)
  },

  getAll() {
    return http.get('/api/vehicle/all')
  }
}
