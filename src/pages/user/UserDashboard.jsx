import { useState, useEffect } from 'react'
import { bookingApi } from '../../api'
import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const STATUS_BADGE = {
  CONFIRMED: 'badge-green',
  CANCELLED: 'badge-red',
  COMPLETED: 'badge-blue',
}

export default function UserDashboard() {
  const { user }  = useAuth()
  const navigate  = useNavigate()
  const [bookings, setBookings]   = useState([])
  const [loading, setLoading]     = useState(true)

  useEffect(() => {
    bookingApi.myList().then(r => setBookings(r.data)).finally(() => setLoading(false))
  }, [])

  const upcoming  = bookings.filter(b => b.status === 'CONFIRMED')
  const completed = bookings.filter(b => b.status === 'COMPLETED').length
  const cancelled = bookings.filter(b => b.status === 'CANCELLED').length

  const nights = (b) => {
    const ms = new Date(b.checkOutDate) - new Date(b.checkInDate)
    return Math.round(ms / 86400000)
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h1>Hello, {user?.name?.split(' ')[0]} 👋</h1>
        <p>Welcome to HotelBook — your travel dashboard</p>
      </div>

      <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(4,1fr)', marginBottom: '1.5rem' }}>
        {[
          { icon: '📅', label: 'Upcoming Stays',  val: upcoming.length,  color: 'var(--gold)'   },
          { icon: '✅', label: 'Completed Stays',  val: completed,        color: 'var(--green)'  },
          { icon: '❌', label: 'Cancelled',        val: cancelled,        color: 'var(--red)'    },
          { icon: '📋', label: 'Total Bookings',   val: bookings.length,  color: 'var(--blue)'   },
        ].map((s, i) => (
          <div key={i} className="stat-card">
            <div className="stat-icon" style={{ fontSize: '1.4rem', background: 'var(--bg3)' }}>{s.icon}</div>
            <div>
              <div className="stat-val" style={{ color: s.color }}>{s.val}</div>
              <div className="stat-lbl">{s.label}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Quick action banner */}
      <div className="card" style={{ marginBottom: '1.5rem', background: 'linear-gradient(135deg,rgba(227,160,8,.1),rgba(63,185,80,.05))', border: '1px solid rgba(227,160,8,.25)' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
          <div>
            <div style={{ fontWeight: 600 }}>Planning your next trip?</div>
            <div style={{ color: 'var(--text2)', fontSize: '.875rem' }}>Browse hotels across India and book instantly</div>
          </div>
          <button className="btn btn-primary" onClick={() => navigate('/user/hotels')}>🔍 Browse Hotels</button>
        </div>
      </div>

      {/* Upcoming bookings */}
      <div className="card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
          <div style={{ fontWeight: 600 }}>Upcoming Stays</div>
          <button className="btn btn-outline btn-sm" onClick={() => navigate('/user/bookings')}>View all</button>
        </div>

        {loading ? <div className="loading">Loading…</div> : upcoming.length === 0 ? (
          <div className="empty">
            <div className="empty-icon">🏨</div>
            <p>No upcoming stays</p>
            <button className="btn btn-primary" style={{ marginTop: '1rem' }} onClick={() => navigate('/user/hotels')}>
              Book your first stay
            </button>
          </div>
        ) : upcoming.slice(0, 3).map(b => (
          <div key={b.id} style={{
            display: 'flex', alignItems: 'center', gap: '1rem',
            padding: '.85rem', background: 'var(--bg3)', borderRadius: 8,
            border: '1px solid var(--border)', marginBottom: '.5rem', flexWrap: 'wrap'
          }}>
            <div style={{
              width: 40, height: 40, borderRadius: 8,
              background: 'var(--gold-dim)', color: 'var(--gold)',
              display: 'flex', alignItems: 'center', justifyContent: 'center',
              fontSize: '1.2rem', flexShrink: 0
            }}>🏨</div>
            <div style={{ flex: 1, minWidth: 0 }}>
              <div style={{ fontWeight: 600 }}>{b.hotelName}</div>
              <div style={{ color: 'var(--text2)', fontSize: '.8rem' }}>
                📍 {b.hotelLocation} · {b.roomType} · {nights(b)} night{nights(b) !== 1 ? 's' : ''}
              </div>
              <div style={{ color: 'var(--text3)', fontSize: '.75rem' }}>
                {b.checkInDate} → {b.checkOutDate}
              </div>
            </div>
            <div style={{ textAlign: 'right' }}>
              <div style={{ color: 'var(--green)', fontWeight: 600 }}>₹{b.totalPrice?.toLocaleString()}</div>
              <span className={`badge ${STATUS_BADGE[b.status]}`}>{b.status}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
