<template>
    <headerNavigation />

    <body class="bodyStructure">
        <div class="structure">
            <div class="headInfo">
                <div class="accountNumber">
                    <p>{{ user.firstName }} {{ user.lastName }}</p>
                </div>
                <div class="groupOptions">
                    <div class="option">
                        <button @click="goToUserInfo()" class="btn">
                            edit
                        </button>
                    </div>
                    <div class="option"></div>
                </div>
            </div>
            <div id="extraPadding">
                <div class="bodyInfo">
                    <div class="account-field" v-for="account in user.bankAccountList" :key="account.id" @click="goToTransactions(account)">
                        <div v-for="accType in account.accountType">
                            <span class="wide-field">{{ accType }} Є{{ account.amount }}</span>
                        </div>
                    </div>
                    <h3 class="total">Total: Є{{ totalAmount }}</h3>
                </div>
            </div>
        </div>
    </body>
    <footerNavigation />
</template>

<script>
import headerNavigation from '../main/Header.vue'
import footerNavigation from '../main/Footer.vue';
import axios from '../../axios-auth.js';
const headerToken = {
    headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
    }
};
export default {
    components: {
        headerNavigation,
        footerNavigation
    },
    data() {
        return {
            user: {
                id: 0,
                username: "",
                password: "",
                firstName: "",
                lastName: "",
                phoneNumber: "",
                email: "",
                street: "",
                houseNumber: "",
                postalCode: "",
                city: "",
                active: "",
                bankAccountList: []
            },
        };
    },
    computed: {
        totalAmount() {
            return this.user.bankAccountList.reduce((total, account) => total + account.amount, 0);
        }
    },
    mounted() {
        const encodedId = this.$route.params.id;
        const decodedId = atob(encodedId);
        this.getUserInfo(decodedId);
    },
    methods: {
        getUserInfo(userId) {
            axios
                .get(`/users/${userId}`, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    this.user = res.data;
                    this.getBankAccounts();
                })
                .catch(error => console.log(error));
        },
        getBankAccounts() {
            // Iterate over the bank accounts and fetch their details
            for (let i = 0; i < this.user.bankAccountList.length; i++) {
                const accountId = this.user.bankAccountList[i];
                axios
                    .get(`/bankaccounts/` + accountId, {
                        headers: {
                            Authorization: "Bearer " + localStorage.getItem("jwt")
                        }
                    })
                    .then((res) => {
                        this.user.bankAccountList[i] = res.data;
                        this.user.bankAccountList[i].amount = res.data.balance;
                        this.user.bankAccountList[i].iban = res.data.iban;
                        this.user.bankAccountList[i].accountType = res.data.accountType;
                    })
                    .catch(error => console.log(error));
            }
            this.bankacc = this.user.bankAccountList[1];
            console.log(this.bankacc);
        },
        goToUserInfo() {
            
            this.$router.push(`/accountInfoforEmployee/` + btoa(this.user.id));
        },
        goToTransactions(account) {
            this.$router.push(`/transactions/` + btoa(account.id));
        },

    },
};
</script>

<style>
@import '../../assets/css/transaction.css';

.wide-field {
    width: 100%;
    display: inline-block;
    text-align: left;
    padding-left: 20px;
}
.account-field {
    width: 100%;
    display: inline-block;
    text-align: left;
    padding-left: 20px;
}
.total {
    padding-left: 20px;
}

</style>
