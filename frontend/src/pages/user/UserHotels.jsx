import { useState, useEffect } from 'react'
import { hotelApi, bookingApi, errMsg } from '../../api'
import toast from 'react-hot-toast'

const TYPE_BADGE = { STANDARD: 'badge-blue', DELUXE: 'badge-gold', SUITE: 'badge-purple', PENTHOUSE: 'badge-yellow' }

function Stars({ rating }) {
  const r = parseFloat(rating) || 0
  return <span className="stars">{'★'.repeat(Math.round(r))}{'☆'.repeat(5 - Math.round(r))} {r.toFixed(1)}</span>
}

function today()    { return new Date().toISOString().split('T')[0] }
function tomorrow() { const d = new Date(); d.setDate(d.getDate() + 1); return d.toISOString().split('T')[0] }

export default function UserHotels() {
  const [hotels, setHotels]         = useState([])
  const [loading, setLoading]       = useState(true)
  const [search, setSearch]         = useState('')
  const [searching, setSearching]   = useState(false)

  // Hotel detail / room view
  const [selected, setSelected]     = useState(null)   // chosen hotel
  const [rooms, setRooms]           = useState([])
  const [roomsLoading, setRoomsLoading] = useState(false)

  // Availability search
  const [checkIn,  setCheckIn]      = useState(today())
  const [checkOut, setCheckOut]     = useState(tomorrow())
  const [searched, setSearched]     = useState(false)

  // Booking modal
  const [bookRoom, setBookRoom]     = useState(null)
  const [guests, setGuests]         = useState(1)
  const [booking, setBooking]       = useState(false)

  useEffect(() => {
    hotelApi.getAll().then(r => setHotels(r.data)).finally(() => setLoading(false))
  }, [])

  const doSearch = async (e) => {
    e.preventDefault()
    if (!search.trim()) { hotelApi.getAll().then(r => setHotels(r.data)); return }
    setSearching(true)
    try {
      const { data } = await hotelApi.search(search.trim())
      setHotels(data)
    } catch {
      toast.error('Search failed')
    } finally { setSearching(false) }
  }

  const openHotel = async (hotel) => {
    setSelected(hotel)
    setRooms([])
    setSearched(false)
    setRoomsLoading(true)
    try {
      const { data } = await hotelApi.getRooms(hotel.id)
      setRooms(data)
    } finally { setRoomsLoading(false) }
  }

  const checkAvailability = async () => {
    if (!checkIn || !checkOut || checkIn >= checkOut) {
      toast.error('Please pick valid check-in / check-out dates'); return
    }
    setRoomsLoading(true)
    try {
      const { data } = await hotelApi.getAvailable(selected.id, checkIn, checkOut)
      setRooms(data); setSearched(true)
    } catch { toast.error('Failed to check availability') }
    finally { setRoomsLoading(false) }
  }

  const confirmBooking = async () => {
    if (!bookRoom) return
    if (!checkIn || !checkOut || checkIn >= checkOut) {
      toast.error('Please set valid dates before booking'); return
    }
    if (guests < 1 || guests > bookRoom.capacity) {
      toast.error(`Guests must be between 1 and ${bookRoom.capacity}`); return
    }
    setBooking(true)
    try {
      await bookingApi.create({
        roomId: bookRoom.id,
        hotelId: selected.id,
        checkInDate: checkIn,
        checkOutDate: checkOut,
        guests,
      })
      const nights = Math.round((new Date(checkOut) - new Date(checkIn)) / 86400000)
      toast.success(`Booking confirmed! ₹${(nights * bookRoom.pricePerNight).toLocaleString()} for ${nights} night${nights !== 1 ? 's' : ''}`)
      setBookRoom(null)
      // refresh rooms
      checkAvailability()
    } catch (err) {
      toast.error(errMsg(err))
    } finally { setBooking(false) }
  }

  const nights = checkIn && checkOut ? Math.max(0, Math.round((new Date(checkOut) - new Date(checkIn)) / 86400000)) : 0

  // ── Hotel list ────────────────────────────────────────────────────────
  if (!selected) return (
    <div className="fade-in">
      <div className="page-header">
        <h1>Browse Hotels</h1>
        <p>Find your perfect stay across India</p>
      </div>

      <form onSubmit={doSearch} style={{ display: 'flex', gap: '.75rem', marginBottom: '1.5rem', flexWrap: 'wrap' }}>
        <input
          className="form-input" style={{ maxWidth: 320 }}
          placeholder="🔍  Search by city — Mumbai, Goa, Shimla…"
          value={search} onChange={e => setSearch(e.target.value)}
        />
        <button type="submit" className="btn btn-primary" disabled={searching}>
          {searching ? 'Searching…' : 'Search'}
        </button>
        {search && (
          <button type="button" className="btn btn-outline" onClick={() => {
            setSearch(''); hotelApi.getAll().then(r => setHotels(r.data))
          }}>Clear</button>
        )}
      </form>

      {loading ? <div className="loading">Loading hotels…</div> : hotels.length === 0 ? (
        <div className="empty"><div className="empty-icon">🏨</div><p>No hotels found for "{search}"</p></div>
      ) : (
        <div className="grid-3">
          {hotels.map(h => (
            <div key={h.id} className="hotel-card" onClick={() => openHotel(h)}>
              {h.imageUrl
                ? <img src={h.imageUrl} alt={h.name} className="hotel-img" onError={e => { e.target.style.display='none' }} />
                : <div className="hotel-img" style={{ display:'flex', alignItems:'center', justifyContent:'center', fontSize:'3rem' }}>🏨</div>
              }
              <div className="hotel-body">
                <div style={{ fontWeight: 700, fontSize: '1.05rem', marginBottom: '.25rem' }}>{h.name}</div>
                <div style={{ color: 'var(--text2)', fontSize: '.82rem', marginBottom: '.35rem' }}>📍 {h.location}</div>
                {h.rating && <Stars rating={h.rating} />}
                {h.amenities && (
                  <div style={{ color: 'var(--text3)', fontSize: '.75rem', marginTop: '.4rem', overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>
                    🎯 {h.amenities}
                  </div>
                )}
                <div style={{ marginTop: '.85rem' }}>
                  <button className="btn btn-primary btn-sm">View Rooms →</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )

  // ── Hotel detail / rooms ──────────────────────────────────────────────
  return (
    <div className="fade-in">
      {/* Back + header */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.25rem' }}>
        <button className="btn btn-outline btn-sm" onClick={() => setSelected(null)}>← Back</button>
        <div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 700 }}>{selected.name}</h1>
          <p style={{ color: 'var(--text2)', fontSize: '.875rem' }}>📍 {selected.location}
            {selected.rating && <span style={{ marginLeft: '.75rem' }}><Stars rating={selected.rating} /></span>}
          </p>
        </div>
      </div>

      {/* Hotel info card */}
      <div className="card" style={{ marginBottom: '1.25rem', display: 'flex', gap: '1.5rem', flexWrap: 'wrap' }}>
        {selected.imageUrl && (
          <img src={selected.imageUrl} alt={selected.name}
            style={{ width: 220, height: 140, objectFit: 'cover', borderRadius: 8, flexShrink: 0 }}
            onError={e => e.target.style.display='none'} />
        )}
        <div style={{ flex: 1, minWidth: 0 }}>
          {selected.description && <p style={{ color: 'var(--text2)', fontSize: '.875rem', marginBottom: '.75rem' }}>{selected.description}</p>}
          {selected.amenities && (
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: '.4rem' }}>
              {selected.amenities.split(',').map(a => (
                <span key={a} className="badge badge-gold">{a.trim()}</span>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Date picker */}
      <div className="card" style={{ marginBottom: '1.25rem' }}>
        <div style={{ fontWeight: 600, marginBottom: '1rem' }}>🗓️ Check Availability</div>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end', flexWrap: 'wrap' }}>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label">Check-In</label>
            <input className="form-input" type="date" min={today()} value={checkIn}
              onChange={e => setCheckIn(e.target.value)} />
          </div>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label">Check-Out</label>
            <input className="form-input" type="date" min={checkIn || today()} value={checkOut}
              onChange={e => setCheckOut(e.target.value)} />
          </div>
          {nights > 0 && (
            <div style={{ color: 'var(--text2)', fontSize: '.85rem', paddingBottom: '.4rem' }}>
              {nights} night{nights !== 1 ? 's' : ''}
            </div>
          )}
          <button className="btn btn-primary" onClick={checkAvailability} disabled={roomsLoading}>
            {roomsLoading ? 'Checking…' : '🔍 Check Available Rooms'}
          </button>
        </div>
      </div>

      {/* Rooms */}
      <div style={{ fontWeight: 600, marginBottom: '.75rem', color: 'var(--text2)', fontSize: '.85rem', textTransform: 'uppercase', letterSpacing: '.04em' }}>
        {searched ? `Available Rooms (${rooms.length})` : `All Rooms (${rooms.length})`}
      </div>

      {roomsLoading ? <div className="loading">Loading rooms…</div> : rooms.length === 0 ? (
        <div className="empty">
          <div className="empty-icon">🛏️</div>
          <p>{searched ? 'No rooms available for these dates' : 'No rooms found'}</p>
        </div>
      ) : (
        <div className="grid-3">
          {rooms.map(r => (
            <div key={r.id} className="room-card">
              {r.imageUrl && (
                <img src={r.imageUrl} alt={r.roomType}
                  style={{ width: '100%', height: 140, objectFit: 'cover', borderRadius: 6, marginBottom: '.85rem' }}
                  onError={e => e.target.style.display='none'} />
              )}
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '.5rem' }}>
                <span className={`badge ${TYPE_BADGE[r.roomType] || 'badge-blue'}`}>{r.roomType}</span>
                <span className={`badge ${r.isAvailable ? 'badge-green' : 'badge-red'}`}>
                  {r.isAvailable ? 'Available' : 'Unavailable'}
                </span>
              </div>
              <div style={{ fontSize: '1.5rem', fontWeight: 700, color: 'var(--gold)', marginBottom: '.25rem' }}>
                ₹{r.pricePerNight?.toLocaleString()}
                <span style={{ fontSize: '.75rem', color: 'var(--text2)', fontWeight: 400 }}> / night</span>
              </div>
              {nights > 0 && (
                <div style={{ color: 'var(--green)', fontSize: '.85rem', marginBottom: '.35rem' }}>
                  Total: ₹{(nights * r.pricePerNight).toLocaleString()} for {nights} night{nights !== 1 ? 's' : ''}
                </div>
              )}
              <div style={{ color: 'var(--text2)', fontSize: '.8rem', marginBottom: '.25rem' }}>
                👥 Up to {r.capacity} guest{r.capacity !== 1 ? 's' : ''}
              </div>
              {r.amenities && (
                <div style={{ color: 'var(--text3)', fontSize: '.75rem', marginBottom: '.85rem' }}>
                  🎯 {r.amenities}
                </div>
              )}
              <button
                className="btn btn-primary"
                style={{ width: '100%', justifyContent: 'center' }}
                disabled={!r.isAvailable}
                onClick={() => { setBookRoom(r); setGuests(1) }}>
                {r.isAvailable ? '🛎️ Book Now' : 'Not Available'}
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Booking confirmation modal */}
      {bookRoom && (
        <div className="modal-overlay" onClick={() => setBookRoom(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setBookRoom(null)}>✕</button>
            <div className="modal-title">🛎️ Confirm Booking</div>

            <div style={{ background: 'var(--bg3)', borderRadius: 8, padding: '1rem', marginBottom: '1.25rem', border: '1px solid var(--border)' }}>
              <div style={{ fontWeight: 600, marginBottom: '.4rem' }}>{selected.name}</div>
              <div style={{ color: 'var(--text2)', fontSize: '.85rem' }}>📍 {selected.location}</div>
              <div style={{ marginTop: '.6rem', display: 'flex', gap: '.5rem', flexWrap: 'wrap' }}>
                <span className={`badge ${TYPE_BADGE[bookRoom.roomType] || 'badge-blue'}`}>{bookRoom.roomType}</span>
                <span style={{ color: 'var(--text2)', fontSize: '.85rem' }}>👥 max {bookRoom.capacity}</span>
              </div>
            </div>

            <div className="grid-2">
              <div className="form-group">
                <label className="form-label">Check-In</label>
                <input className="form-input" type="date" min={today()} value={checkIn}
                  onChange={e => setCheckIn(e.target.value)} />
              </div>
              <div className="form-group">
                <label className="form-label">Check-Out</label>
                <input className="form-input" type="date" min={checkIn} value={checkOut}
                  onChange={e => setCheckOut(e.target.value)} />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Number of Guests (max {bookRoom.capacity})</label>
              <input className="form-input" type="number" min="1" max={bookRoom.capacity}
                value={guests} onChange={e => setGuests(Number(e.target.value))} />
            </div>

            {nights > 0 && (
              <div style={{ background: 'var(--gold-dim)', border: '1px solid rgba(227,160,8,.3)', borderRadius: 8, padding: '1rem', marginBottom: '1rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '.875rem', marginBottom: '.3rem' }}>
                  <span style={{ color: 'var(--text2)' }}>₹{bookRoom.pricePerNight?.toLocaleString()} × {nights} night{nights !== 1 ? 's' : ''}</span>
                  <span style={{ fontWeight: 600, color: 'var(--gold)' }}>₹{(nights * bookRoom.pricePerNight).toLocaleString()}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontWeight: 700 }}>
                  <span>Total</span>
                  <span style={{ color: 'var(--gold)', fontSize: '1.1rem' }}>₹{(nights * bookRoom.pricePerNight).toLocaleString()}</span>
                </div>
              </div>
            )}

            <div className="modal-actions">
              <button className="btn btn-outline" onClick={() => setBookRoom(null)}>Cancel</button>
              <button className="btn btn-primary" onClick={confirmBooking} disabled={booking || nights < 1}>
                {booking ? 'Booking…' : '✅ Confirm Booking'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
