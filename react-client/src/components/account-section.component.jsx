
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
            return (<li key={ac.id} >{ac.name}</li>)
        });
        var investAccounts = this.state.insuranceAccounts.map(ac => {
            return (<li key={ac.id} >{ac.name}</li>)
        });

        return (
            <div className="account-section">
                <div className="banking">
                    <h1 className="banking-title">Banking</h1>
                    {bankAccounts}
                </div>
                <div className="investment">
                <h1 className="banking-title">Investment</h1>
                    {investAccounts}
                </div>
            </div>
        );
    }
}

export default AccountSection;