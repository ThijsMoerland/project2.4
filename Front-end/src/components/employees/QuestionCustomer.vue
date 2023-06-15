<template>
    <headerNavigation />

    <body>
        <div id="container">
            <h1>Already a customer?</h1>

            <div id="btns">
                <button id="btn" @click="addBankAccount()">Yes</button>
                <button id="btn" @click="addUser()">No</button>
            </div>
        </div>
    </body>
    <footerNavigation />
</template>
  
<script>
import headerNavigation from '../main/Header.vue'
import footerNavigation from '../main/Footer.vue';

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
        }
    },
    methods: {
        addUser() {
            this.$router.push("/AddUser");
        },
        addBankAccount() {
            this.$router.push("/employee/AddBankAccount");
        },
    },
    
};
</script>
  
<style>
@import '../../assets/css/addAccount.css';
</style>