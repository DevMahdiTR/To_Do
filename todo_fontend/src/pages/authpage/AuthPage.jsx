import './authpage.scss'
import { SingIn, SingUp } from '../../components/index'
const AuthPage = () => {
  return (
    <div className='auth_page'>
      <div className='auth_page__container'>
        <div className="sing-components">
          <SingIn  />
          <SingUp />
        </div>
        <div className='information '>
          <h3> errogergergergergergergergr :</h3>
        </div>
      </div>
    </div>
  )
}

export default AuthPage
