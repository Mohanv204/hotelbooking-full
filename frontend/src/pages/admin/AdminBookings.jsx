import { useState, useEffect } from 'react'
import { adminApi, errMsg } from '../../api'
import toast from 'react-hot-toast'

const STATUS_BADGE = {
  CONFIRMED: 'badge-green',
  CANCELLED: 'badge-red',
  COMPLETED: 'badge-blue',
}

export default function AdminBookings() {
  const [bookings, setBookings] = useState([])
  const [filtered, setFiltered] = useState([])
  const [loading, setLoading]   = useState(true)
  const [search, setSearch]     = useState('')
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [updatingId, setUpdatingId]     = useState(null)

  const load = () =>
    adminApi.getBookings()
      .then(r => { setBookings(r.data); setFiltered(r.data) })
      .finally(() => setLoading(false))

  useEffect(() => { load() }, [])

  useEffect(() => {
    let list = bookings
    if (statusFilter !== 'ALL') list = list.filter(b => b.status === statusFilter)
    if (search.trim()) {
      const q = search.toLowerCase()
      list = list.filter(b =>
        b.userName?.toLowerCase().includes(q) ||
        b.hotelName?.toLowerCase().includes(q) ||
        b.hotelLocation?.toLowerCase().includes(q) ||
        String(b.id).includes(q)
      )
    }
    setFiltered(list)
  }, [search, statusFilter, bookings])

  const updateStatus = async (id, status) => {
    setUpdatingId(id)
    try {
      await adminApi.updateStatus(id, status)
      toast.success(`Booking marked as ${status}`)
      load()
    } catch (err) {
      toast.error(errMsg(err))
    } finally { setUpdatingId(null) }
  }

  const nights = (b) => {
    const ms = new Date(b.checkOutDate) - new Date(b.checkInDate)
    return Math.round(ms / 86400000)
  }

  return (
    <div className="fade-in">
      <div className="page-header">
        <h1>All Bookings</h1>
        <p>View and manage every booking across all hotels</p>
      </div>

      {/* Filters */}
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        <input
          className="form-input" style={{ maxWidth: 280 }}
          placeholder="🔍  Search by name, hotel, ID…"
          value={search} onChange={e => setSearch(e.target.value)}
        />
        {['ALL', 'CONFIRMED', 'CANCELLED', 'COMPLETED'].map(s => (
          <button key={s}
            className={`btn ${statusFilter === s ? 'btn-primary' : 'btn-outline'} btn-sm`}
            onClick={() => setStatusFilter(s)}>
            {s}
          </button>
        ))}
        <span style={{ marginLeft: 'auto', color: 'var(--text2)', fontSize: '.85rem', alignSelf: 'center' }}>
          {filtered.length} booking{filtered.length !== 1 ? 's' : ''}
        </span>
      </div>

      <div className="card" style={{ padding: 0 }}>
        {loading ? (
          <div className="loading">Loading bookings…</div>
        ) : filtered.length === 0 ? (
          <div className="empty"><div className="empty-icon">📋</div><p>No bookings found</p></div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#ID</th>
                  <th>Guest</th>
                  <th>Hotel</th>
                  <th>Room</th>
                  <th>Dates</th>
                  <th>Nights</th>
                  <th>Guests</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(b => (
                  <tr key={b.id}>
                    <td style={{ color: 'var(--text2)', fontFamily: 'monospace' }}>#{b.id}</td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{b.userName}</div>
                      <div style={{ color: 'var(--text2)', fontSize: '.75rem' }}>{b.userEmail}</div>
                    </td>
                    <td>
                      <div style={{ fontWeight: 500 }}>{b.hotelName}</div>
                      <div style={{ color: 'var(--text2)', fontSize: '.75rem' }}>📍 {b.hotelLocation}</div>
                    </td>
                    <td><span className="badge badge-gold">{b.roomType}</span></td>
                    <td style={{ fontSize: '.8rem' }}>
                      <div>{b.checkInDate}</div>
                      <div style={{ color: 'var(--text2)' }}>→ {b.checkOutDate}</div>
                    </td>
                    <td style={{ textAlign: 'center' }}>{nights(b)}</td>
                    <td style={{ textAlign: 'center' }}>{b.guests}</td>
                    <td style={{ color: 'var(--green)', fontWeight: 600 }}>₹{b.totalPrice?.toLocaleString()}</td>
                    <td><span className={`badge ${STATUS_BADGE[b.status] || 'badge-yellow'}`}>{b.status}</span></td>
                    <td>
                      {b.status === 'CONFIRMED' && (
                        <div style={{ display: 'flex', gap: '.4rem' }}>
                          <button
                            className="btn btn-green btn-sm"
                            disabled={updatingId === b.id}
                            onClick={() => updateStatus(b.id, 'COMPLETED')}>
                            ✅
                          </button>
                          <button
                            className="btn btn-red btn-sm"
                            disabled={updatingId === b.id}
                            onClick={() => updateStatus(b.id, 'CANCELLED')}>
                            ✕
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
