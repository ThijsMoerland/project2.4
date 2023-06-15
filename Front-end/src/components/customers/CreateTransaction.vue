<template>
    <headerNavigation />

    <body class="bodyStructure">
        <div class="structure">
            <div class="headInfo">
                <div class="accountNumber">
                    <input type="text" class="input" placeholder="rekening van" :value="this.bankaccount.iban" id="fromInput">
                </div>
                <div class="accountNumber mr-2">
                    <input  type="text" class="input" placeholder="rekening naar" v-model="transaction.bankAccountTo" list="ibanList" id="bankaccountTo" >
                    <datalist id="ibanList" >
                        <option v-for="nameAndDto in this.nameAndDtoList" :value="nameAndDto.iban">{{ nameAndDto.name }} | {{ nameAndDto.iban }} ({{ nameAndDto.accountType }}) </option>
                    </datalist>

                </div>
            </div>
            <div class="body">
                <div class="other" id="dropdown" style="display: none;">
                    <input  type="text" class="input" placeholder="rekening naar" list="ibanLists" v-model="this.accountID">
                    <datalist id="ibanLists" >
                        <option v-for="bankaccount in this.usersBankList" :value="bankaccount.id"> 
                            <div v-for="accType in bankaccount.accountType">
                            {{ bankaccount.iban }} ({{ accType }}) 
                            </div>
                        </option>
                    </datalist>
                </div>
                <div class="other">
                    <input type="text" class="input" placeholder="bedrag" v-model="transaction.amount">
                </div>
                <div class="other">
                    <input type="text" class="input" placeholder="description" v-model="transaction.description">
                </div>
                <div class="other">
                    <input type="text" class="input" placeholder="betalingskenmerk" v-model="transaction.paymentReference">
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
    name: "createtransactions",
    props: {
        id: Number,
    },
    data() {
        return {
            accountID: 0,
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
            transaction:
            {
                id: 0,
                description: "",
                amount: 0,
                accountTypeFrom: [],
                accountTypeTo: [],
                bankAccountFrom: "",
                bankAccountTo: "",
                paymentReference: "",
                date: "",
                performedByUser: [],
            },
            pincode: "",
            bankaccount : {
                id: 0,
                iban: "",
                balance: 0,
                userId: 0,
                disabled: false,
                currencies: [],
                accountType:[],
                absoluutLimit: 0,
            },
            otherBankAccount :
            {
                id: 0,
                iban: "",
                balance: 0,
                userId: 0,
                disabled: false,
                currencies: [],
                accountType:[],
                absoluutLimit: 0,
            },
            nameAndDtoList: {
                id: 0,
                Name: "",
                Iban: "",
                accountType: [],
            },
            usersBankList : [],
            decodedId: atob(this.id),
        };
    },
    mounted() {
        this.getUser();
        this.getBankAccount();
        this.getNameAndDtoList();
        

        document.getElementById("bankaccountTo").addEventListener("change", () => {
            this.checkAccountIban();
        });
    },
    methods: {
        safething(id){
            alert("yeah" +id);
            // this.otherBankAccount.id = id;
        },
        getNameAndDtoList(){
            axios
                .get('bankaccounts/All', {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    this.nameAndDtoList = res.data;
                    console.log(this.nameAndDtoList);
                })
                .catch(error => console.log(error))
        },

        fillfield(){
            let fromInput = document.getElementById("fromInput");
            if(this.user.roles[0] == "CUSTOMER"){
                fromInput.setAttribute( 'readonly', true );
            }
        },
        checkAccountIban(){
            if(this.bankaccount.iban == this.transaction.bankAccountTo){
                document.getElementById("dropdown").style.display = "block";
                this.transaction.accountTypeFrom .push(this.bankaccount.accountType[0]);
            }else {
                document.getElementById("dropdown").style.display = "none";
                this.transaction.accountTypeFrom.push("CURRENT");
                this.transaction.accountTypeTo.push("CURRENT");
            }
        },getBankAccountById(id) {
            axios
                .get('/bankaccounts/userID/' + id, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    this.usersBankList = res.data;
                    console.log(res.data);
                })
                .catch(error => console.log(error))
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
                    
                    this.transaction.performedByUser = res.data;
                    this.fillfield();
                })
                .catch(error => console.log(error))
        },
        getBankAccount() {
            axios
                .get('/bankaccounts/' + this.decodedId, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    this.bankaccount = res.data;
                    this.getBankAccountById(this.bankaccount.userId);
                    this.transaction.bankAccountFrom = this.bankaccount.iban;
                    console.log(this.bankaccount);
                    
                })
                .catch(error => console.log(error))
        },

        
        showPincode() {
            
            document.getElementById("test").style.display = "table";
        },
        closePincode() {
            document.getElementById("test").style.display = "none";
        },
        postTransaction(){
          this.transaction.date = new Date();
          alert("i am in");
          console.log(this.transaction);

          axios
                    .post('transactions',this.transaction, {
                        headers: {
                            Authorization: "Bearer " + localStorage.getItem("jwt")
                        }
                    })
                    .then((res) => {
                        console.log(res.data);
                        this.updateBalance();
                    })
                    .catch((error) => console.log(error));
          
        },
        verifyRequest(){
            if(this.otherBankAccount.iban == this.bankaccount.iban){
                if(this.otherBankAccount.accountType[0] == "CURRENT" && this.bankaccount.accountType[0] == "CURRENT" || this.otherBankAccount.accountType[0] == "SAVINGS" && this.bankaccount.accountType[0] == "SAVINGS"){
                    alert("you can't transfer to the same account");
                    location.reload();
                    return;
                }
                
            }
            else {
                if(this.otherBankAccount.accountType[0] != "CURRENT"){
                    alert("you can only transfer to a current account");
                    location.reload();
                    return;
                }
            }
            if(this.bankaccount.iban == this.transaction.bankAccountTo && this.accountID == ""){
                alert("please fill in wich accountID");
                location.reload();
                return;
            }
            
            if(this.otherBankAccount.disabled == true || this.bankaccount.disabled == true){
                alert("you can't transfer to and / or from a disabled account");
                location.reload();
                return;
            }
            if(this.transaction.amount > this.user.transactionLimit){
                alert("you can't transfer more than your transaction limit");
                location.reload();
                return;
            }

            let amount = parseInt(this.transaction.amount);

            let newbalance = this.bankaccount.balance -= amount;
            let othernewbalance = this.otherBankAccount.balance += amount;

            if(newbalance < this.bankaccount.absoluutLimit || this.newbalance < 0){
                alert("you will end below your absolute limit or below 0");
                location.reload();
                return;
            }
            this.bankaccount.balance = newbalance;
            this.otherBankAccount.balance = othernewbalance;

            var dailylimit = this.user.dailyLimit - this.transaction.amount;
            if(dailylimit < 0){
                alert("you will end below your daily limit");
                location.reload();
                return;
            }
            this.postTransaction();
        },
        getBankAccountByIban(){
            axios
                .get('bankaccounts/iban/'+this.transaction.bankAccountTo,{
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    if(res.data == null || res.data == undefined || res.data == ""){
                            alert("this account doesn't exist");
                            location.reload();
                        }
                    this.otherBankAccount = res.data;
                    this.transaction.bankAccountTo = this.otherBankAccount.iban;
                    this.verifyRequest();
                    console.log(this.otherBankAccount);
                    
                })
                .catch(error => console.log(error))
        },
        getOtherBankAccount(){
            if(this.accountID == 0){
                this.getBankAccountByIban();
            }else{
                axios
                .get('/bankaccounts/' + this.accountID, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    if(res.data == null || res.data == undefined || res.data == ""){
                            alert("this account doesn't exist");
                            location.reload();
                        }
                    this.otherBankAccount = res.data;
                    this.transaction.bankAccountTo = this.otherBankAccount.iban;
                    this.transaction.accountTypeTo .push(this.otherBankAccount.accountType[0]);
                    this.verifyRequest();
                    console.log(this.otherBankAccount);
                    
                })
                .catch(error => console.log(error))
            }
            
        },
        updateBalance(){
            axios
                .put('bankaccounts/change/' + this.decodedId, this.bankaccount, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    console.log(res.data);
                })
                .catch((error) => console.log(error));

            axios
            .put('bankaccounts/change/' + this.otherBankAccount.id, this.otherBankAccount, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    console.log(res.data)
                    this.$router.push("/transactions/" + btoa(this.decodedId));
                })
                .catch((error) => console.log(error));
        },
        checkPincode() {
            console.log(this.pincode);
            axios
                .get('users/pincode/' + this.pincode, {
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("jwt")
                    }
                })
                .then((res) => {
                    console.log(res.data)
                    if(res.data != ""){
                        this.getOtherBankAccount();
                    }
                    else{
                        alert("wrong pincode")
                    }
                })
                .catch((error) => console.log(error));
        },
    },
};

</script>

<style>
@import '../../assets/css/transaction.css';
</style>
