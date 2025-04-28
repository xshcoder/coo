import { FC, InputHTMLAttributes } from 'react';
import styles from './Input.module.scss';

type InputRadius = 'sm' | 'm' | 'l';

interface InputProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'size'> {
  radius?: InputRadius;
  iconLeft?: string; // Add prop for left icon (SVG path/URL)
  className?: string; // Ensure className is part of the props
}

const Input: FC<InputProps> = ({
  radius = 'm',
  disabled = false,
  className,
  placeholder,
  iconLeft, // Destructure the new prop
  ...rest
}) => {
  // Build the class string for the input element itself
  const inputClasses = [
    styles.input,
    styles[`input-radius-${radius}`],
    disabled ? styles['input-disabled'] : '',
    iconLeft ? styles['input-with-icon-left'] : '', // Add class if icon exists
    className // Allow passing additional classes to the input itself if needed
  ].filter(Boolean).join(' ');

  // Build the class string for the wrapper
  const wrapperClasses = [
    styles['input-wrapper'],
    className // Apply external className to the wrapper
  ].filter(Boolean).join(' ');


  return (
    <div className={wrapperClasses}>
      {iconLeft && (
        <img src={iconLeft} alt="Input icon" className={styles['input-icon-left']} />
      )}
      <input
        className={inputClasses} // Apply input-specific classes here
        disabled={disabled}
        placeholder={placeholder}
        {...rest}
      />
    </div>
  );
};

export default Input;