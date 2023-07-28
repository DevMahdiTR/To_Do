
import { Navigate } from 'react-router-dom'
import {useLocalState} from '../../util/useLocalStorage'
import { useEffect, useState } from 'react'

const PrivateRoute = ({children}) => {
  
    const [jwt , setJwt] = useLocalState("", "jwt")
    const [valid,setValid] = useState(true)

    useEffect(()=>{
        
        const checkToken = async () => {
            try{
                const response = await fetch(`http://localhost:8080/api/v1/token/validate/${jwt.jwt}`, {
                    method: 'GET'
                })
                const data = await response.json()
                setValid(true)
                console.log(data)
            }catch(error)
            {
                setValid(false)
            }
        }
        checkToken();
    })

    return valid ? children :<Navigate to="/login" />
}

export default PrivateRoute
