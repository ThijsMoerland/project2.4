<template>
    <div class="container">
        <h2>Personal Details</h2>
        <div>
            <label>User:</label>
            <select v-model="this.selectedUser" @change="checkAccountType()">
                <option v-for="user in this.users" :value="user">{{ user.username }}</option>
            </select>
        </div>
        <div>
            <div v-if="this.accountTypes != null">
                <label>Account type:</label>
                <select v-model="this.selectedAccountType">
                    <option v-for="accountType in this.accountTypes" :value="accountType">{{ accountType }}</option>
                </select>
            </div>
            <div v-else>
                This user already has the maximum of bankaccounts possible.
            </div>
        </div>
        <div>
            <button id="btn2" class="btnUpdate" @click="cancel()">Cancel</button>
            <button id="btn2" @click="getIbanOfUser()">Save Changes</button>
        </div>
    </div>
</template>

  
<script>
import axios from '../../axios-auth.js';

const headerToken = {
    headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
    }
};

export default {

    data() {
        return {
            users: [],
            selectedUser: {},
            generatedIban: '',
            bankAccounts: [],
            accountTypes: ['CURRENT', 'SAVINGS'],
            selectedAccountType: '',
            newBankAccount:
            {
                id: 0,
                iban: '',
                balance: 0,
                userId: 0,
                disabled: false,
                currencies: "EUR",
                accountType: [],
            },
        };
    },
    mounted() {
        this.getUsers();
    },
    methods: {
        cancel() {
            this.$router.push("/employee/question");
        },
        getUsers() {
            axios
                .get('/users', headerToken)
                .then((res) => {
                    this.users = res.data;
                })
                .catch(error => console.log(error));
        },
        generateIBAN() {
            const countryCode = 'NL';
            const additionalDigits = Math.floor(Math.random() * 100).toString().padStart(2, '0');
            const bankCode = 'INHO';
            const accountNumber = Math.floor(Math.random() * 10000000000).toString().padStart(10, '0');

            this.generatedIban = `${countryCode}${additionalDigits}${bankCode}0${accountNumber}`;
            this.checkIbanExists();
        },
        checkIbanExists() {
            axios
                .get('bankaccounts', headerToken)
                .then((res) => {
                    this.bankAccount = res.data;

                    for (const element of this.bankAccount) {
                        if (element.iban == this.generatedIban) {
                            this.generateIBAN();
                        }
                    }
                }).catch((error) => console.log(error));
        },
        checkAccountType() {
            axios
                .get('bankaccounts', headerToken)
                .then((res) => {
                    this.bankAccount = res.data;
                    let savingsAccountsCount = 5;
                    let currentsAccountsCount = 1;

                    for (const element of this.bankAccount) {
                        if (element.userId == this.selectedUser.id) {

                            if (element.accountType == 'CURRENT') {
                                currentsAccountsCount--;
                            }
                            else if (element.accountType == 'SAVINGS') {
                                savingsAccountsCount--;
                            }
                        }
                    }

                    if (savingsAccountsCount <= 0 && currentsAccountsCount <= 0) {
                        this.accountTypes = null
                    }
                    else if (savingsAccountsCount <= 0) {
                        this.accountTypes = ['CURRENT']
                    }
                    else if (currentsAccountsCount <= 0) {
                        this.accountTypes = ['SAVINGS']
                    }

                }).catch((error) => console.log(error));
        },
        getIbanOfUser() {
            axios
                .get('/bankaccounts', headerToken)
                .then((res) => {
                    this.bankAccounts = res.data;

                    for (const element of this.bankAccounts) {
                        if (this.selectedUser.id == element.userId) {
                            this.newBankAccount.iban = element.iban;
                            this.ibanExists = true;
                            break;
                        }
                    }

                    if (!this.ibanExists) {
                        this.generateIBAN();
                        this.newBankAccount.iban = this.generatedIban;
                    }

                    this.addBankAccount();

                }).catch((error) => console.log(error));
        },
        addBankAccount() {
            this.newBankAccount.accountType = [];
            this.newBankAccount.accountType.push(this.selectedAccountType);
            this.newBankAccount.userId = this.selectedUser.id;

            axios
                .post('bankaccounts', this.newBankAccount, headerToken)
                .then((res) => {
                    this.updateUserBankList(res.data.id);
                    this.$router.push("/allAccounts");
                })
                .catch((error) => console.log(error));

        },
        updateUserBankList(id) {
            this.selectedUser.bankAccountList.push(id)

            axios
                .put('users/' + this.selectedUser.id, this.selectedUser, headerToken)
                .then((res) => {
                    console.log(res.data)
                })
                .catch((error) => console.log(error));
        },

    },
};
</script>
  
<style>
@import '../../assets/css/addAccount.css';
</style>
  