import { LOG_IN, LOG_OUT } from "../redux/actions/types";
import { LoginActionTypes } from "./userActions";
import { UserState } from "./UserState";

const INITIAL_STATE: UserState = {
  isLogined: false,
  username: undefined,
}

const userReducer = (state = INITIAL_STATE, action: LoginActionTypes): UserState => {
  switch(action.type) {
    case LOG_IN:
      return {
        ...state,
        isLogined: action.payload.isLogined,
        username: action.payload.username
      }
    case LOG_OUT:
      return {
        ...state,
        isLogined: action.payload.isLogined
      }
    default: 
    return state;

  }
}

export default userReducer;