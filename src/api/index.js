import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 12000 })

http.interceptors.request.use(cfg => {
  const t = localStorage.getItem('hotel_token')
  if (t) cfg.headers.Authorization = `Bearer ${t}`
  return cfg
})

http.interceptors.response.use(
  r => r,
  err => {
    if (err.response?.status === 401 && !err.config.url.includes('/auth/')) {
      localStorage.removeItem('hotel_token')
      localStorage.removeItem('hotel_user')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

export const errMsg = (err) =>
  err.response?.data?.message ||
  err.response?.data?.error ||
  (err.code === 'ERR_NETWORK' ? 'Cannot reach backend at port 9091. Is it running?' : null) ||
  err.message ||
  'Something went wrong'

// ── Auth ──────────────────────────────────────────────────────────────────
export const authApi = {
  login:    d => http.post('/auth/login', d),
  register: d => http.post('/auth/register', d),
}

// ── Hotels (public) ───────────────────────────────────────────────────────
export const hotelApi = {
  getAll:         ()                        => http.get('/hotels'),
  search:         location                  => http.get('/hotels/search', { params: { location } }),
  getById:        id                        => http.get(`/hotels/${id}`),
  getRooms:       hotelId                   => http.get(`/hotels/${hotelId}/rooms`),
  getAvailable:   (hotelId, checkIn, checkOut) =>
    http.get(`/hotels/${hotelId}/rooms/available`, { params: { checkIn, checkOut } }),
}

// ── Bookings (user) ───────────────────────────────────────────────────────
export const bookingApi = {
  create:   d  => http.post('/bookings', d),
  myList:   () => http.get('/bookings/my'),
  getById:  id => http.get(`/bookings/${id}`),
  cancel:   id => http.put(`/bookings/${id}/cancel`),
}

// ── Admin ─────────────────────────────────────────────────────────────────
export const adminApi = {
  dashboard:    ()           => http.get('/admin/dashboard'),
  getHotels:    ()           => http.get('/admin/hotels'),
  createHotel:  d            => http.post('/admin/hotels', d),
  updateHotel:  (id, d)      => http.put(`/admin/hotels/${id}`, d),
  addRoom:      d            => http.post('/admin/rooms', d),
  getBookings:  ()           => http.get('/admin/bookings'),
  updateStatus: (id, status) => http.put(`/admin/bookings/${id}/status`, null, { params: { status } }),
}

export default http
