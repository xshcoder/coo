import { FC } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGoogle, faApple } from '@fortawesome/free-brands-svg-icons';
import cooLogo from '../assets/img/coo.svg';

const Login: FC = () => {
  return (
    <div className="login-container">
      <div className="login-content">
        <div className="logo-section">
          <img src={cooLogo} alt="Coo Logo" />
        </div>

        <div className="auth-section">
          <div className="auth-header">
            <h1>Happening now</h1>
            <h2>Join today.</h2>
          </div>

          <div className="auth-buttons">
            <button className="btn btn-google">
              <FontAwesomeIcon icon={faGoogle} />
              Sign in with Google
            </button>
            <button className="btn btn-apple">
              <FontAwesomeIcon icon={faApple} />
              Sign in with Apple
            </button>

            <div className="divider">or</div>

            <button className="btn btn-signup">
              Create account
            </button>

            <p className="terms">
              By signing up, you agree to the Terms of Service and Privacy Policy, including Cookie Use.
            </p>

            <div className="signin-section">
              <p>Already have an account?</p>
              <a href="#" className="signin-link">Sign in</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;