import { useState, useEffect } from 'react'
import { bookingApi, errMsg } from '../../api'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'

const STATUS_BADGE = {
  CONFIRMED: 'badge-green',
  CANCELLED: 'badge-red',
  COMPLETED: 'badge-blue',
}

export default function UserBookings() {
  const [bookings, setBookings]   = useState([])
  const [filtered, setFiltered]   = useState([])
  const [loading, setLoading]     = useState(true)
  const [statusFilter, setStatus] = useState('ALL')
  const [cancelId, setCancelId]   = useState(null)
  const [cancelling, setCancelling] = useState(false)
  const navigate = useNavigate()

  const load = () =>
    bookingApi.myList().then(r => { setBookings(r.data); setFiltered(r.data) }).finally(() => setLoading(false))

  useEffect(() => { load() }, [])

  useEffect(() => {
    setFiltered(statusFilter === 'ALL' ? bookings : bookings.filter(b => b.status === statusFilter))
  }, [statusFilter, bookings])

  const doCancel = async () => {
    setCancelling(true)
    try {
      await bookingApi.cancel(cancelId)
      toast.success('Booking cancelled. Refund in 5–7 business days.')
      setCancelId(null); load()
    } catch (err) {
      toast.error(errMsg(err))
    } finally { setCancelling(false) }
  }

  const nights = (b) => Math.max(0, Math.round((new Date(b.checkOutDate) - new Date(b.checkInDate)) / 86400000))

  return (
    <div className="fade-in">
      <div className="page-header-row page-header">
        <div>
          <h1>My Bookings</h1>
          <p>Track and manage your hotel reservations</p>
        </div>
        <button className="btn btn-primary" onClick={() => navigate('/user/hotels')}>🔍 New Booking</button>
      </div>

      {/* Filter tabs */}
      <div style={{ display: 'flex', gap: '.5rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {['ALL', 'CONFIRMED', 'COMPLETED', 'CANCELLED'].map(s => (
          <button key={s}
            className={`btn ${statusFilter === s ? 'btn-primary' : 'btn-outline'} btn-sm`}
            onClick={() => setStatus(s)}>
            {s === 'ALL' ? `All (${bookings.length})` : `${s} (${bookings.filter(b => b.status === s).length})`}
          </button>
        ))}
      </div>

      {loading ? <div className="loading">Loading bookings…</div> : filtered.length === 0 ? (
        <div className="empty">
          <div className="empty-icon">📋</div>
          <p>{statusFilter === 'ALL' ? "You haven't made any bookings yet" : `No ${statusFilter.toLowerCase()} bookings`}</p>
          {statusFilter === 'ALL' && (
            <button className="btn btn-primary" style={{ marginTop: '1rem' }} onClick={() => navigate('/user/hotels')}>
              Book your first stay 🏨
            </button>
          )}
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {filtered.map(b => (
            <div key={b.id} className="card" style={{ padding: '1.25rem' }}>
              <div style={{ display: 'flex', gap: '1.25rem', flexWrap: 'wrap' }}>
                {/* Left: hotel icon */}
                <div style={{
                  width: 56, height: 56, borderRadius: 10,
                  background: 'var(--gold-dim)', color: 'var(--gold)',
                  display: 'flex', alignItems: 'center', justifyContent: 'center',
                  fontSize: '1.6rem', flexShrink: 0
                }}>🏨</div>

                {/* Middle: details */}
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '.5rem' }}>
                    <div>
                      <div style={{ fontWeight: 700, fontSize: '1rem' }}>{b.hotelName}</div>
                      <div style={{ color: 'var(--text2)', fontSize: '.82rem' }}>📍 {b.hotelLocation}</div>
                    </div>
                    <div style={{ display: 'flex', gap: '.5rem', alignItems: 'center' }}>
                      <span className={`badge ${STATUS_BADGE[b.status] || 'badge-yellow'}`}>{b.status}</span>
                      <span style={{ color: 'var(--text3)', fontSize: '.75rem', fontFamily: 'monospace' }}>#{b.id}</span>
                    </div>
                  </div>

                  <div style={{ display: 'flex', gap: '1.5rem', marginTop: '.75rem', flexWrap: 'wrap' }}>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Room</div>
                      <div style={{ fontSize: '.875rem', fontWeight: 500 }}>{b.roomType}</div>
                    </div>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Check-In</div>
                      <div style={{ fontSize: '.875rem', fontWeight: 500 }}>{b.checkInDate}</div>
                    </div>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Check-Out</div>
                      <div style={{ fontSize: '.875rem', fontWeight: 500 }}>{b.checkOutDate}</div>
                    </div>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Duration</div>
                      <div style={{ fontSize: '.875rem', fontWeight: 500 }}>{nights(b)} night{nights(b) !== 1 ? 's' : ''}</div>
                    </div>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Guests</div>
                      <div style={{ fontSize: '.875rem', fontWeight: 500 }}>{b.guests}</div>
                    </div>
                    <div>
                      <div style={{ color: 'var(--text3)', fontSize: '.7rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>Total</div>
                      <div style={{ fontSize: '.975rem', fontWeight: 700, color: 'var(--green)' }}>₹{b.totalPrice?.toLocaleString()}</div>
                    </div>
                  </div>
                </div>
              </div>

              {b.status === 'CONFIRMED' && (
                <div style={{ marginTop: '1rem', paddingTop: '1rem', borderTop: '1px solid var(--border)', display: 'flex', justifyContent: 'flex-end' }}>
                  <button className="btn btn-red btn-sm" onClick={() => setCancelId(b.id)}>
                    ✕ Cancel Booking
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}

      {/* Cancel confirm modal */}
      {cancelId && (
        <div className="modal-overlay" onClick={() => setCancelId(null)}>
          <div className="modal" style={{ maxWidth: 400 }} onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setCancelId(null)}>✕</button>
            <div className="modal-title">Cancel Booking #{cancelId}?</div>
            <p style={{ color: 'var(--text2)', fontSize: '.875rem', marginBottom: '1.25rem' }}>
              This action cannot be undone. Refund will be processed in 5–7 business days.
            </p>
            <div className="modal-actions">
              <button className="btn btn-outline" onClick={() => setCancelId(null)}>Keep Booking</button>
              <button className="btn btn-red" onClick={doCancel} disabled={cancelling}>
                {cancelling ? 'Cancelling…' : 'Yes, Cancel'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
