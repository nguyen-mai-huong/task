import { Typography } from '@material-ui/core';

export default function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {'Copyright © '}
      {'Singapore Examinations and Assessment Board '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  )
}