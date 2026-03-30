import { useState, useEffect } from 'react'
import { adminApi } from '../../api'
import toast from 'react-hot-toast'

const EMPTY_HOTEL = { name: '', location: '', description: '', rating: '', amenities: '', imageUrl: '' }
const EMPTY_ROOM  = { hotelId: '', roomType: 'STANDARD', pricePerNight: '', capacity: '', amenities: '', imageUrl: '' }
const ROOM_TYPES  = ['STANDARD', 'DELUXE', 'SUITE', 'PENTHOUSE']

function Stars({ rating }) {
  const r = parseFloat(rating) || 0
  return <span className="stars">{'★'.repeat(Math.round(r))}{'☆'.repeat(5 - Math.round(r))} {r.toFixed(1)}</span>
}

export default function AdminHotels() {
  const [hotels, setHotels]       = useState([])
  const [loading, setLoading]     = useState(true)
  const [hotelModal, setHotelModal] = useState(false)
  const [roomModal, setRoomModal]   = useState(false)
  const [editing, setEditing]     = useState(null)   // hotel being edited
  const [hotelForm, setHotelForm] = useState(EMPTY_HOTEL)
  const [roomForm, setRoomForm]   = useState(EMPTY_ROOM)
  const [saving, setSaving]       = useState(false)

  const load = () => adminApi.getHotels().then(r => setHotels(r.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  // ── Hotel save ────────────────────────────────────────────────────────
  const saveHotel = async (e) => {
    e.preventDefault(); setSaving(true)
    try {
      if (editing) {
        await adminApi.updateHotel(editing.id, hotelForm)
        toast.success('Hotel updated!')
      } else {
        await adminApi.createHotel(hotelForm)
        toast.success('Hotel created!')
      }
      setHotelModal(false); setEditing(null); setHotelForm(EMPTY_HOTEL); load()
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed')
    } finally { setSaving(false) }
  }

  const openEdit = (h) => {
    setEditing(h)
    setHotelForm({ name: h.name, location: h.location, description: h.description || '',
      rating: h.rating || '', amenities: h.amenities || '', imageUrl: h.imageUrl || '' })
    setHotelModal(true)
  }

  // ── Room save ─────────────────────────────────────────────────────────
  const saveRoom = async (e) => {
    e.preventDefault(); setSaving(true)
    try {
      await adminApi.addRoom({ ...roomForm, pricePerNight: Number(roomForm.pricePerNight), capacity: Number(roomForm.capacity), hotelId: Number(roomForm.hotelId) })
      toast.success('Room added!')
      setRoomModal(false); setRoomForm(EMPTY_ROOM)
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed')
    } finally { setSaving(false) }
  }

  const hf = k => ({ value: hotelForm[k], onChange: e => setHotelForm(p => ({ ...p, [k]: e.target.value })) })
  const rf = k => ({ value: roomForm[k],  onChange: e => setRoomForm(p => ({ ...p, [k]: e.target.value })) })

  return (
    <div className="fade-in">
      <div className="page-header-row page-header">
        <div>
          <h1>Hotels Management</h1>
          <p>Add, edit hotels and attach rooms</p>
        </div>
        <div style={{ display: 'flex', gap: '.75rem' }}>
          <button className="btn btn-outline" onClick={() => { setRoomForm(EMPTY_ROOM); setRoomModal(true) }}>➕ Add Room</button>
          <button className="btn btn-primary" onClick={() => { setEditing(null); setHotelForm(EMPTY_HOTEL); setHotelModal(true) }}>🏨 Add Hotel</button>
        </div>
      </div>

      {loading ? <div className="loading">Loading hotels…</div> : (
        <div className="grid-3">
          {hotels.map(h => (
            <div key={h.id} className="hotel-card">
              {h.imageUrl && <img src={h.imageUrl} alt={h.name} className="hotel-img" onError={e => e.target.style.display='none'} />}
              <div className="hotel-body">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '.4rem' }}>
                  <div style={{ fontWeight: 700, fontSize: '1rem' }}>{h.name}</div>
                  <span className={`badge ${h.isActive ? 'badge-green' : 'badge-red'}`}>{h.isActive ? 'Active' : 'Inactive'}</span>
                </div>
                <div style={{ color: 'var(--text2)', fontSize: '.8rem', marginBottom: '.35rem' }}>📍 {h.location}</div>
                {h.rating && <Stars rating={h.rating} />}
                {h.amenities && <div style={{ color: 'var(--text3)', fontSize: '.75rem', marginTop: '.4rem', lineClamp: 1, overflow: 'hidden', whiteSpace: 'nowrap', textOverflow: 'ellipsis' }}>🎯 {h.amenities}</div>}
                <div style={{ marginTop: '.85rem' }}>
                  <button className="btn btn-outline btn-sm" onClick={() => openEdit(h)}>✏️ Edit</button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Hotel Modal */}
      {hotelModal && (
        <div className="modal-overlay" onClick={() => setHotelModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setHotelModal(false)}>✕</button>
            <div className="modal-title">{editing ? '✏️ Edit Hotel' : '🏨 New Hotel'}</div>
            <form onSubmit={saveHotel}>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Hotel Name</label>
                  <input className="form-input" required placeholder="The Grand Palace" {...hf('name')} />
                </div>
                <div className="form-group">
                  <label className="form-label">Location</label>
                  <input className="form-input" required placeholder="Mumbai" {...hf('location')} />
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Description</label>
                <textarea className="form-input" placeholder="Describe the hotel…" {...hf('description')} />
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Rating (1–5)</label>
                  <input className="form-input" type="number" step="0.1" min="1" max="5" placeholder="4.5" {...hf('rating')} />
                </div>
                <div className="form-group">
                  <label className="form-label">Image URL</label>
                  <input className="form-input" placeholder="https://…" {...hf('imageUrl')} />
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Amenities (comma-separated)</label>
                <input className="form-input" placeholder="Pool, WiFi, Gym, Spa" {...hf('amenities')} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-outline" onClick={() => setHotelModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : editing ? 'Update Hotel' : 'Create Hotel'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Room Modal */}
      {roomModal && (
        <div className="modal-overlay" onClick={() => setRoomModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={() => setRoomModal(false)}>✕</button>
            <div className="modal-title">🛏️ Add Room</div>
            <form onSubmit={saveRoom}>
              <div className="form-group">
                <label className="form-label">Hotel</label>
                <select className="form-input" required {...rf('hotelId')}>
                  <option value="">Select hotel…</option>
                  {hotels.map(h => <option key={h.id} value={h.id}>{h.name} — {h.location}</option>)}
                </select>
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Room Type</label>
                  <select className="form-input" {...rf('roomType')}>
                    {ROOM_TYPES.map(t => <option key={t}>{t}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Price / Night (₹)</label>
                  <input className="form-input" type="number" required min="1" placeholder="2500" {...rf('pricePerNight')} />
                </div>
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Capacity (guests)</label>
                  <input className="form-input" type="number" required min="1" placeholder="2" {...rf('capacity')} />
                </div>
                <div className="form-group">
                  <label className="form-label">Image URL</label>
                  <input className="form-input" placeholder="https://…" {...rf('imageUrl')} />
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Amenities</label>
                <input className="form-input" placeholder="AC, TV, WiFi, Minibar" {...rf('amenities')} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-outline" onClick={() => setRoomModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Saving…' : 'Add Room'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
