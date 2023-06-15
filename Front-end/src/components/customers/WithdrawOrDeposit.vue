<template>
    <headerNavigation />

    <body class="bodyStructure">
        <div class="structure">
            <div class="headInfo">
                <div class="accountNumber">
                    <input type="text" class="input" placeholder="van rekeningnummer...." :value=this.bankAccount.iban id="fromInput">
                </div>
            </div>
            <div class="body">
                <div class="other">
                    <input type="number" class="input" placeholder="bedrag" v-model="bedrag">
                </div>
                <div class="other">
                    <input type="text" class="input" placeholder="description" v-model="omscrijving">
                </div>
                <div class="other">
                    <select name="choice" v-model="choice">
                        <option value="withdraw">withdraw</option>
                        <option value="deposit">deposit</option>
                    </select>
                </div>
            </div>
            <button @click="showPincode()">Submit</button>
        </div>
    </body>
    <footerNavigation />


    <Transition name="modal">
        <div class="modal-mask" id="test">
            <div class="modal-wrapper">
                <div class="modal-container">
                    <div class="modal-header">
                        <slot name="header">Please enter your pincode</slot>
                    </div>

                    <div class="modal-body">
                        <input type="text" class="input" v-model="pincode">
                    </div>

                    <div class="modal-footer">
                        <slot name="footer">
                            <button class="modal-default-button" @click="closePincode()">close</button>
                            <button class="modal-default-button" @click="checkPincode()">OK</button>
                        </slot>
                    </div>
                </div>
            </div>
        </div>
    </Transition>
</template>

<style>
.input {
    max-width: 100%;
    border: 1px solid black !important;
}

.modal-mask {
    display: none;
    position: fixed;
    z-index: 9998;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    transition: opacity 0.3s ease;
}

.modal-wrapper {
    display: table-cell;
    vertical-align: middle;
}

.modal-container {
    width: 300px;
    margin: 0px auto;
    padding: 20px 30px;
    background-color: #fff;
    border-radius: 2px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
    transition: all 0.3s ease;
}

.modal-header h3 {
    margin-top: 0;
    color: #42b983;
}

.modal-body {
    margin: 20px 0;
}

.modal-default-button {
    float: right;
}

/*
   * The following styles are auto-applied to elements with
   * transition="modal" when their visibility is toggled
   * by Vue.js.
   *
   * You can easily play with the modal transition by editing
   * these styles.
   */

.modal-enter-from {
    opacity: 0;
}

.modal-leave-to {
    opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
    -webkit-transform: scale(1.1);
    transform: scale(1.1);
}
</style>

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
    header: {
        name: "header",
        components: {
            headerNavigation
        }
    },
    footer: {
        name: "footer",
        components: {
            footerNavigation
        },
    },
    name: "withdrawOrDeposit",
    props: {
        id: Number,
    },
    data() {
        return {
            rekening: "",
            bedrag: 0,
            omscrijving: "",
            choice: "",
            bankAccount:
            {
                id: 0,
                iban: '',
                balance: 0,
                userId: 0,
                disabled: '',
                currencies: [],
                accountType: [],
                absoluutLimit: 0,
            },
            user:
            {
                id: 0,
                username: "",
                password: "",
                fistName: "",
                lastName: "",
                phoneNumber: "",
                email: "",
                street: "",
                houseNumber: "",
                postalCode: "",
                city: "",
                bankAccountList: [],
                roles: [],
                dailyLimit: 0,
                transactionLimit: 0,
            },
        };
    },
    mounted() {
        this.getUser();
        this.getBankAccount();

    },
    methods: {
        fillfield(){
            let fromInput = document.getElementById("fromInput");
            console.log(this.user.roles[0]);
            if(this.user.roles[0]== "CUSTOMER"){
                fromInput.setAttribute( 'readonly', true );
            }
        },
        getUser() {
            axios
                .get('users/current', {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    this.user = res.data;
                    this.fillfield();
                })
                .catch(error => console.log(error))
        },
        getBankAccount() {
            const decodedId = atob(this.id)
            axios
                .get('/bankaccounts/' + decodedId, headerToken)
                .then((res) => {
                    this.bankAccount = res.data;
                    this.rekening = this.bankAccount.iban;
                    console.log(this.bankAccount);

                })
                .catch(error => console.log(error))
        },
        showPincode() {
            this.rekening = document.getElementById("fromInput").value;
            document.getElementById("test").style.display = "table";
        },
        closePincode() {
            document.getElementById("test").style.display = "none";
        },
        checkPincode() {
            axios
                .get('users/pincode/' + this.pincode, headerToken)
                .then((res) => {
                    console.log(res.data)
                    // console.log(this.bankAccount);
                    this.withdrawOrDeposit();
                })
                .catch((error) => console.log(error));

        },
        withdrawOrDeposit() {
            if (this.choice == "withdraw") {
                this.withdraw();
            } else if (this.choice == "deposit") {
                this.deposit();
            }
            else {
                alert("please select withdraw or deposit");
                location.reload();
            }
        },
        withdraw() {
            let newbalance = this.bankAccount.balance -= this.bedrag;
            this.verifyRequest(newbalance);
            this.bankAccount.balance = newbalance;
            this.changeBankAcount();
        },
        deposit() {
            let newbalance = this.bankAccount.balance += this.bedrag;
            this.verifyRequest(newbalance);
            this.bankAccount.balance = newbalance;
            this.changeBankAcount();
        },
        verifyRequest(newbalance) {
            if (this.bankAccount.accountType[0] == "SAVINGS") {
                alert("you can't withdraw or deposit from or to a savings account");
                location.reload();
            }
            if (this.bankAccount.disabled == true) {
                alert("you can't withdraw or deposit from or to a disabled account");
                location.reload();
            }
            if (this.choice == "withdraw" && newbalance < this.bankAccount.absoluutLimit || newbalance < 0) {
                alert("you can't withdraw or deposit more than your absoluut limit");
                location.reload();
            }
            var dailylimit = this.user.dailyLimit - this.bedrag;
            if (dailylimit < 0) {
                alert("you can't withdraw or deposit more than your daily limit");
                location.reload();
            }
            if (this.rekening == "" || this.bedrag == 0 || this.choice == "") {
                alert("you need to fill in all the fields");
                location.reload();
            }
            if(this.bedrag < 0){
                alert("you can't withdraw or deposit a negative amount");
                location.reload();
            }
        },
        changeBankAcount() {
            const decodedId = atob(this.id)
            axios
                .put("/bankaccounts/change/" + decodedId, this.bankAccount, headerToken)
                .then((res) => {
                    console.log(res.data);
                    this.$router.push("/transactions/" + btoa(decodedId));
                })
                .catch((error) => console.log(error));
        }
    },
};
</script>

<style>
@import '../../assets/css/transaction.css';

.structure {
    max-width: 90%;
}
</style>
