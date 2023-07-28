import './singin.scss'
import { EastIcon } from '../../assets/index'
import { useState } from 'react'
import axios from 'axios'
import { Navigate } from 'react-router-dom'

import {useLocalState} from '../../util/useLocalStorage'
const SingIn = () => {

    
    const [jwt , setJwt] = useLocalState(null, "jwt")
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loggedIn, setLoggedIn] = useState(false); 

    const handleUsernameChange = (event) => {
        setUsername(event.target.value)
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value)
    }
   
    const handleSubmit = async (event) => {
        event.preventDefault();
        const payload = {
            username,
            password,
        };

        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/login', payload);

            const data = response.data;
            setJwt({ jwt: data.token});
            setLoggedIn(true);

        } catch (error) {
            const responseError = error.response.data.errors[0]
            const information = document.querySelector('.information')
            information.innerHTML = responseError;
            information.style.opacity = '1';
            information.style.backgroundColor = 'rgba(255, 148, 148, 0.781)';
            information.style.border = '2px solid red';
            setTimeout(() => {
              information.style.opacity = '0';
            },5000);
        }
    };
    if (loggedIn) {
        // If loggedIn is true, render the Navigate component to redirect to the "todo" page
        return <Navigate to="/todo" />;
    }
    return (
        <div className='sing_in'>
            <div className='sing_in__container'>
                <div className='sing_in__container__title'>
                    <strong>Welcome!</strong>
                    <span>Sing in to your account</span>
                </div>
                <form className='sing_in__container__form'>
                    <div className='sing_in__container__form__input-username input-label'>
                        <p>Name</p>
                        <input
                            type="text"
                            className='form-text'
                            onChange={handleUsernameChange}
                        />
                        <div className="input-line"></div>
                        <div className="on-select"></div>
                    </div>
                    <div className='sing_in__container__form__input-password input-label'>
                        <p>Password</p>
                        <input
                            type="password"
                            className='form-text'
                            onChange={handlePasswordChange}
                        />
                        <div className="input-line"></div>
                        <div className="on-select"></div>
                    </div>
                    <div className='sing_in__container__form__input-submit'>
                        <button
                            type='submit'
                            className='form-submit'
                            onClick={handleSubmit}
                        >
                            Login
                            <EastIcon />
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default SingIn
