import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi, errMsg } from '../api'
import toast from 'react-hot-toast'

export default function Register() {
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '' })
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate  = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const { data } = await authApi.register(form)
      login(data)
      toast.success('Account created! Welcome to HotelBook 🏨')
      navigate('/user')
    } catch (err) {
      toast.error(errMsg(err))
    } finally { setLoading(false) }
  }

  const f = key => ({ value: form[key], onChange: e => setForm(p => ({ ...p, [key]: e.target.value })) })

  return (
    <div style={{
      minHeight: '100vh', display: 'flex', alignItems: 'center',
      justifyContent: 'center', background: 'var(--bg)', padding: '1rem'
    }}>
      <div style={{ width: '100%', maxWidth: 400 }} className="fade-in">
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{
            width: 56, height: 56, background: 'var(--green)', borderRadius: 14,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '1.8rem', margin: '0 auto 1rem'
          }}>✨</div>
          <h1 style={{ fontFamily: 'Playfair Display, serif', fontSize: '1.75rem' }}>Create Account</h1>
          <p style={{ color: 'var(--text2)', fontSize: '.875rem', marginTop: '.25rem' }}>Join HotelBook today</p>
        </div>

        <div className="card">
          <form onSubmit={submit}>
            <div className="form-group">
              <label className="form-label">Full Name</label>
              <input className="form-input" required placeholder="Rahul Sharma" {...f('name')} />
            </div>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" required placeholder="you@example.com" {...f('email')} />
            </div>
            <div className="form-group">
              <label className="form-label">Phone</label>
              <input className="form-input" placeholder="9876543210" {...f('phone')} />
            </div>
            <div className="form-group">
              <label className="form-label">Password (min 6 chars)</label>
              <input className="form-input" type="password" required minLength={6} placeholder="••••••••" {...f('password')} />
            </div>
            <button type="submit" className="btn btn-primary"
              style={{ width: '100%', justifyContent: 'center', padding: '.7rem' }}
              disabled={loading}>
              {loading ? 'Creating…' : '🏨 Create Account'}
            </button>
          </form>
          <p style={{ textAlign: 'center', marginTop: '1rem', fontSize: '.85rem', color: 'var(--text2)' }}>
            Have an account? <Link to="/login" style={{ color: 'var(--gold)' }}>Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  )
}
