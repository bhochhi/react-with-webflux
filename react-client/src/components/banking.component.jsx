
import React, { Component } from 'react';

class Banking extends Component {

  constructor(props, context) {
    super(props, context);

    this.state = {
      checked: false
    };

    this.customMethod = this.customMethod.bind(this);
  }

  customMethod() {
    return { accounts: ["bank1", "bank2"] }
  }

  render() {
    var data = this.customMethod().accounts.map(ac => {
      return (<li key={ac} >{ac}</li>)
    });
    return (
      <div className="banking">
        {data}
      </div>
    );
  }
}

export default Banking;