import React from 'react';
import ReactDOM from 'react-dom'; // this is recommended
import logo from './logo.svg';
import './App.css';

function App() {
  return (
      ReactDOM.render(
          <h1>Hello, world!</h1>,
          document.getElementById('root')
      )
  );
}

export default App;
