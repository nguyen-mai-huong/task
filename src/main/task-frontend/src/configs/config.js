const config = {
  /* username */
  username: {
    minLength: 6,
    minLowerCase: 1,
    minUpperCase: 1,
    minSpecialChar: 0,
    minNumericVal: 0
  },

  /* password */
  password: {
    minLength: 8,
    minLowerCase: 1,
    minUpperCase: 1,
    minSpecialChar: 1,
    minNumericVal: 1
  }
};

export default config;
