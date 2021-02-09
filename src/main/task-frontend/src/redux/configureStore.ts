import { rootReducer } from './reducers';
import { createStore } from 'redux';

export default () => {
  const store = createStore(rootReducer);
  return store;
}