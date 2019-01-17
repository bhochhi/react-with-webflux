
import React, { Component } from 'react';

class AccountSection extends Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            bankAccounts: props.bankAccounts,
            insuranceAccounts: props.insuranceAccounts
        };
    }

    componentWillReceiveProps(nextProps) {
		this.setState({ insuranceAccounts: nextProps.insuranceAccounts, bankAccounts:nextProps.bankAccounts});  
    }

    render() {
        var bankAccounts = this.state.bankAccounts.map(ac => {
            return (<li key={ac} >{ac}</li>)
        });
        var investAccounts = this.state.insuranceAccounts.map(ac => {
            return (<li key={ac} >{ac}</li>)
        });

        return (
            <div className="account-section">
                <div className="banking">
                    <h1>Banking</h1>
                    {bankAccounts}
                </div>
                <hr/>
                <div className="investment">
                <h1>Investment</h1>
                    {investAccounts}
                </div>
            </div>
        );
    }
}

export default AccountSection;