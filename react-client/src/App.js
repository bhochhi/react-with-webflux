import React, {
  Component
} from 'react';
import logo from './logo.svg';
import './App.css';
import AccountSection from './components/account-section.component';
import ActivitySection from './components/activity-section.component';
import {
  range,
  Observable,
  combineLatest
} from 'rxjs';

import {Observable as Obs} from 'rxjs/Observable'
import {
  map,
  filter
} from 'rxjs/operators';
import {
  ajax
} from 'rxjs/ajax';

class App extends Component {
  constructor(props, context) {
    super(props, context);

    this.state = {
      bankAccounts: ["bankAccounts1", "bankAccounts2"],
      insuranceAccounts: ["insuranceAccounts1", "insuranceAccounts2"],
      transactions: ["transaction1", "transaction2"]
    };
  }


  componentDidMount() {

    const bank = [{
        name: 'bankAc1'
      },
      {
        name: 'bankAc2'
      },
      {
        name: 'bankAc3'
      },
      {
        name: 'bankAc4'
      }
    ]
    const insurance = [{
        name: 'insurance1'
      },
      {
        name: 'insurance2'
      },
      {
        name: 'insurance3'
      },
      {
        name: 'insurance4'
      }
    ]

    let trans = [{
        name: 'trans1 desc'
      },
      {
        name: 'trans2 desc'
      },
      {
        name: 'trans3 desc'
      },
      {
        name: 'trans4 desc'
      }
    ]



    const banking$ = ajax({
      url: 'https://httpbin.org/post',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: bank
    });

    const insurance$ = ajax({
      url: 'https://httpbin.org/post',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: insurance
    });

    const transactions$ = ajax({
      url: 'https://httpbin.org/post',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: trans
    });

    const subscription = combineLatest(banking$, insurance$, transactions$)
      .subscribe(([bankRes, insuranceRes,transRes]) => {
        let bankAccountData = bankRes.response.json.map(e => e.name);
        let insuranceAccountData = insuranceRes.response.json.map(e => e.name);
        let transactonData = transRes.response.json.map(e => e.name);

        //simulating delay
        setTimeout(() => {
          this.setState({
            bankAccounts: bankAccountData
          });
        }, 4000);
        setTimeout(() => this.setState({
          insuranceAccounts: insuranceAccountData
        }), 3000);
        setTimeout(() => this.setState({
          transactions: transactonData
        }), 1000);

      }, e => console.error(e));

  }

  


  render() {
    return ( 
    <div className = "App" >
      <AccountSection bankAccounts = {
        this.state.bankAccounts
      }
      insuranceAccounts = {
        this.state.insuranceAccounts
      }
      /> 
      <hr />
      <ActivitySection bills = {
        this.state.bills
      }
      transactions = {
        this.state.transactions
      }
      /> 
      </div>
    );
  }
}
export default App;