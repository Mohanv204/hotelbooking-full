import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const NAV = {
  ADMIN: [
    { to: '/admin',           label: 'Dashboard', icon: '📊' },
    { to: '/admin/hotels',    label: 'Hotels',    icon: '🏨' },
    { to: '/admin/bookings',  label: 'Bookings',  icon: '📋' },
  ],
  USER: [
    { to: '/user',            label: 'Dashboard', icon: '📊' },
    { to: '/user/hotels',     label: 'Browse Hotels', icon: '🔍' },
    { to: '/user/bookings',   label: 'My Bookings',   icon: '📋' },
  ],
}

const ROLE_COLOR = { ADMIN: '#e3a008', USER: '#3fb950' }
const ROLE_BG    = { ADMIN: 'rgba(227,160,8,.15)', USER: 'rgba(63,185,80,.15)' }

export default function Sidebar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const items = NAV[user?.role] || []

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <div className="logo-icon">🏨</div>
        <span className="logo-text">HotelBook</span>
      </div>

      <div className="sidebar-user">
        <div className="user-info">
          <div className="user-avatar"
            style={{ background: ROLE_BG[user?.role], color: ROLE_COLOR[user?.role] }}>
            {user?.name?.[0]?.toUpperCase()}
          </div>
          <div>
            <div className="user-name"
              style={{ maxWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
              {user?.name}
            </div>
            <div className="user-role" style={{ color: ROLE_COLOR[user?.role] }}>
              {user?.role}
            </div>
          </div>
        </div>
      </div>

      <nav className="sidebar-nav">
        {items.map(({ to, label, icon }) => (
          <NavLink key={to} to={to} end={to.split('/').length === 2}
            className={({ isActive }) => `nav-item${isActive ? ' active' : ''}`}>
            <span>{icon}</span>
            {label}
          </NavLink>
        ))}
      </nav>

      <div className="sidebar-footer">
        <button className="btn btn-outline" style={{ width: '100%', justifyContent: 'center' }}
          onClick={() => { logout(); navigate('/login') }}>
          🚪 Sign Out
        </button>
      </div>
    </aside>
  )
}
