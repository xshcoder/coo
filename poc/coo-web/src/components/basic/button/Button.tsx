import { FC, ButtonHTMLAttributes } from 'react';
import styles from './Button.module.scss';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  primary?: boolean;
}

const Button: FC<ButtonProps> = ({
  primary = false,
  disabled = false,
  className,
  children,
  ...rest
}) => {
  // Manually build the class string
  const buttonClasses = [
    styles.button, // Base button style
    primary && !disabled ? styles['button-primary'] : '', // Primary style (only if not disabled)
    disabled ? styles['button-disabled'] : '', // Disabled style
    className // Allow passing additional classes
  ].filter(Boolean).join(' '); // Filter out empty strings and join with spaces


  return (
    <button className={buttonClasses} disabled={disabled} {...rest}>
      {children}
    </button>
  );
};

export default Button;