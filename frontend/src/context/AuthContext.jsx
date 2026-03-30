import { createContext, useContext, useState, useEffect } from 'react'

const Ctx = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser]   = useState(null)
  const [token, setToken] = useState(null)
  const [ready, setReady] = useState(false)

  useEffect(() => {
    const t = localStorage.getItem('hotel_token')
    const u = localStorage.getItem('hotel_user')
    if (t && u) { setToken(t); setUser(JSON.parse(u)) }
    setReady(true)
  }, [])

  const login = (data) => {
    const u = { userId: data.userId, name: data.name, email: data.email, role: data.role }
    setToken(data.token); setUser(u)
    localStorage.setItem('hotel_token', data.token)
    localStorage.setItem('hotel_user', JSON.stringify(u))
  }

  const logout = () => {
    setToken(null); setUser(null)
    localStorage.removeItem('hotel_token')
    localStorage.removeItem('hotel_user')
  }

  return <Ctx.Provider value={{ user, token, login, logout, ready }}>{children}</Ctx.Provider>
}

export const useAuth = () => useContext(Ctx)
