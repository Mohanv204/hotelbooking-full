import { useState, useEffect } from 'react'
import { adminApi } from '../../api'

export default function AdminDashboard() {
  const [stats, setStats] = useState(null)
  useEffect(() => { adminApi.dashboard().then(r => setStats(r.data)).catch(() => {}) }, [])

  const cards = stats ? [
    { icon: '📋', label: 'Total Bookings',     val: stats.totalBookings,     bg: 'var(--blue-dim)',   color: 'var(--blue)'   },
    { icon: '✅', label: 'Confirmed',           val: stats.confirmedBookings, bg: 'var(--green-dim)',  color: 'var(--green)'  },
    { icon: '❌', label: 'Cancelled',           val: stats.cancelledBookings, bg: 'var(--red-dim)',    color: 'var(--red)'    },
    { icon: '🏁', label: 'Completed',           val: stats.completedBookings, bg: 'var(--purple-dim)', color: 'var(--purple)' },
    { icon: '🏨', label: 'Total Hotels',        val: stats.totalHotels,       bg: 'var(--gold-dim)',   color: 'var(--gold)'   },
    { icon: '👤', label: 'Registered Users',    val: stats.totalUsers,        bg: 'var(--blue-dim)',   color: 'var(--blue)'   },
    { icon: '💰', label: 'Total Revenue',       val: `₹${stats.totalRevenue.toLocaleString()}`, bg: 'var(--green-dim)', color: 'var(--green)' },
  ] : []

  return (
    <div className="fade-in">
      <div className="page-header">
        <h1>Admin Dashboard</h1>
        <p>Hotel booking overview &amp; analytics</p>
      </div>

      {!stats ? <div className="loading">Loading stats…</div> : (
        <div className="stats-grid" style={{ gridTemplateColumns: 'repeat(4, 1fr)' }}>
          {cards.map((c, i) => (
            <div key={i} className="stat-card">
              <div className="stat-icon" style={{ background: c.bg }}>
                <span style={{ fontSize: '1.3rem' }}>{c.icon}</span>
              </div>
              <div>
                <div className="stat-val" style={{ color: c.color }}>{c.val}</div>
                <div className="stat-lbl">{c.label}</div>
              </div>
            </div>
          ))}
        </div>
      )}

      <div className="card" style={{ marginTop: '1.5rem', background: 'linear-gradient(135deg, rgba(227,160,8,.08), rgba(63,185,80,.05))' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <span style={{ fontSize: '1.5rem' }}>🏨</span>
          <div>
            <div style={{ fontWeight: 600 }}>System Running on Port 9091</div>
            <div style={{ color: 'var(--text2)', fontSize: '.875rem' }}>
              All services operational • MySQL connected • JWT Auth active
            </div>
          </div>
          <span className="badge badge-green" style={{ marginLeft: 'auto' }}>LIVE</span>
        </div>
      </div>
    </div>
  )
}
