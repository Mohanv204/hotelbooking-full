import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { authApi, errMsg } from '../api'
import toast from 'react-hot-toast'

export default function Login() {
  const [form, setForm] = useState({ email: '', password: '' })
  const [loading, setLoading] = useState(false)
  const [showPw, setShowPw] = useState(false)
  const { login } = useAuth()
  const navigate  = useNavigate()

  const submit = async (e) => {
    e.preventDefault()
    setLoading(true)
    try {
      const { data } = await authApi.login(form)
      login(data)
      toast.success(`Welcome back, ${data.name}!`)
      navigate(`/${data.role.toLowerCase()}`)
    } catch (err) {
      toast.error(errMsg(err))
    } finally { setLoading(false) }
  }

  const fill = (email, pw) => setForm({ email, password: pw })

  return (
    <div style={{
      minHeight: '100vh', display: 'flex', alignItems: 'center',
      justifyContent: 'center', background: 'var(--bg)', padding: '1rem'
    }}>
      <div style={{ width: '100%', maxWidth: 400 }} className="fade-in">
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{
            width: 56, height: 56, background: 'var(--gold)', borderRadius: 14,
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            fontSize: '1.8rem', margin: '0 auto 1rem',
            boxShadow: '0 8px 32px rgba(227,160,8,.3)'
          }}>🏨</div>
          <h1 style={{ fontFamily: 'Playfair Display, serif', fontSize: '1.75rem' }}>HotelBook</h1>
          <p style={{ color: 'var(--text2)', fontSize: '.875rem', marginTop: '.25rem' }}>
            Find &amp; Book Hotels Across India
          </p>
        </div>

        <div className="card">
          <h2 style={{ fontSize: '1rem', fontWeight: 600, marginBottom: '1.25rem' }}>Sign In</h2>
          <form onSubmit={submit}>
            <div className="form-group">
              <label className="form-label">Email</label>
              <input className="form-input" type="email" required placeholder="you@example.com"
                value={form.email} onChange={e => setForm(p => ({ ...p, email: e.target.value }))} />
            </div>
            <div className="form-group">
              <label className="form-label">Password</label>
              <div style={{ position: 'relative' }}>
                <input className="form-input" type={showPw ? 'text' : 'password'} required
                  placeholder="••••••••" style={{ paddingRight: '2.5rem' }}
                  value={form.password} onChange={e => setForm(p => ({ ...p, password: e.target.value }))} />
                <button type="button" onClick={() => setShowPw(!showPw)} style={{
                  position: 'absolute', right: '.75rem', top: '50%', transform: 'translateY(-50%)',
                  background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text2)', fontSize: '.9rem'
                }}>{showPw ? '🙈' : '👁️'}</button>
              </div>
            </div>
            <button type="submit" className="btn btn-primary"
              style={{ width: '100%', justifyContent: 'center', padding: '.7rem' }}
              disabled={loading}>
              {loading ? 'Signing in…' : 'Sign In'}
            </button>
          </form>

          <div style={{
            marginTop: '1.25rem', padding: '.85rem',
            background: 'var(--bg3)', borderRadius: 8,
            border: '1px solid var(--border)', fontSize: '.8rem'
          }}>
            <div style={{ color: 'var(--text3)', marginBottom: '.5rem', textTransform: 'uppercase', letterSpacing: '.04em', fontSize: '.7rem' }}>
              Quick Demo Login
            </div>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '.3rem' }}>
              {[
                { label: '🟡 Admin', email: 'admin@hotel.com', pw: 'admin123' },
                { label: '🟢 User',  email: 'user@hotel.com',  pw: 'user123' },
              ].map(({ label, email, pw }) => (
                <button key={email} onClick={() => fill(email, pw)} style={{
                  background: 'none', border: 'none', cursor: 'pointer',
                  color: 'var(--gold)', textAlign: 'left', padding: '.15rem 0', fontSize: '.8rem'
                }}>
                  {label}: {email} / {pw}
                </button>
              ))}
            </div>
          </div>

          <p style={{ textAlign: 'center', marginTop: '1rem', fontSize: '.85rem', color: 'var(--text2)' }}>
            New here?{' '}
            <Link to="/register" style={{ color: 'var(--gold)' }}>Create account</Link>
          </p>
        </div>
      </div>
    </div>
  )
}
