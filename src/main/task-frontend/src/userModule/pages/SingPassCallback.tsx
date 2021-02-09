import { useCallback, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import httpClient from '../../httpClient';
import { logIn } from '../userActions';
import { selectIsLogined } from '../userSelector';
import { UserState } from '../UserState';

import { Redirect } from 'react-router-dom';

const SingPassCallback = () => {

  const dispatch = useDispatch();
  const isLogined = useSelector(selectIsLogined);
  
  const sendAuthCodeToAppServer = useCallback(() => {
    const callbackParams = new URLSearchParams(window.location.search);
    if (callbackParams.get('code') === undefined || callbackParams.get('code') === null){
      return false;
    }

    const authCode: string = callbackParams.get('code')!;
    const params = new URLSearchParams();
    params.append("code", authCode);
    
    httpClient.post('login/oauth2/code/singpass', params).then((response) => {
      console.log("Response after sending auth code is: ", response);

      if (response.data.code === "ok" && response.data.idToken) {
        const successUserLogin: UserState = {
          isLogined: true,
          username: response.data.username
        }
        dispatch(logIn(successUserLogin));
      }
    }).catch((error) => {
      console.log("Error is: ", error);  
    })
    
  }, []); 

  useEffect(() => {
    sendAuthCodeToAppServer();
  }, [sendAuthCodeToAppServer]);

  if (isLogined) {
    return (
      <Redirect to="/home" />
    )
  } 

  return (
    <div>
      Sending auth code to app server 
    </div>
  )
}

export default SingPassCallback;