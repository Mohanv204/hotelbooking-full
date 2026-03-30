import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import { AuthProvider, useAuth } from './context/AuthContext'
import Sidebar from './components/Sidebar'

import Login    from './pages/Login'
import Register from './pages/Register'

import AdminDashboard from './pages/admin/AdminDashboard'
import AdminHotels    from './pages/admin/AdminHotels'
import AdminBookings  from './pages/admin/AdminBookings'

import UserDashboard from './pages/user/UserDashboard'
import UserHotels    from './pages/user/UserHotels'
import UserBookings  from './pages/user/UserBookings'

function Protected({ role }) {
  const { user, ready } = useAuth()
  if (!ready) return <div className="loading">Loading…</div>
  if (!user)  return <Navigate to="/login" replace />
  if (user.role !== role) return <Navigate to="/login" replace />
  return (
    <div className="layout">
      <Sidebar />
      <main className="main"><Outlet /></main>
    </div>
  )
}

function Public() {
  const { user, ready } = useAuth()
  if (!ready) return <div className="loading">Loading…</div>
  if (user) return <Navigate to={`/${user.role.toLowerCase()}`} replace />
  return <Outlet />
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Toaster position="top-right" toastOptions={{
          style: { background: '#21262d', color: '#e6edf3', border: '1px solid #30363d' }
        }} />
        <Routes>
          <Route element={<Public />}>
            <Route path="/login"    element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>

          <Route element={<Protected role="ADMIN" />}>
            <Route path="/admin"          element={<AdminDashboard />} />
            <Route path="/admin/hotels"   element={<AdminHotels />} />
            <Route path="/admin/bookings" element={<AdminBookings />} />
          </Route>

          <Route element={<Protected role="USER" />}>
            <Route path="/user"           element={<UserDashboard />} />
            <Route path="/user/hotels"    element={<UserHotels />} />
            <Route path="/user/bookings"  element={<UserBookings />} />
          </Route>

          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}
