import { UserState } from "./UserState";
import { LOG_IN, LOG_OUT } from "../redux/actions/types";

interface LogInAction {
  type: typeof LOG_IN,
  payload: UserState
}

interface LogOutAction {
  type: typeof LOG_OUT,
  payload: UserState
}

export type LoginActionTypes =
  | LogInAction
  | LogOutAction;

export function logIn(payload: UserState): LoginActionTypes {
  return {
    type: LOG_IN,
    payload: payload
  };
}

export function logOut(payload: UserState): LoginActionTypes {
  return {
    type: LOG_OUT,
    payload: payload
  };
}

