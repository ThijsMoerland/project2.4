<template>
  <headerNavigation />

  <div class="container">
    <h2>Personal Details</h2>
    <div>
      <label>First Name:</label>
      <span>{{ customer.firstName }}</span>
    </div>
    <div>
      <label>Last Name:</label>
      <span>{{ customer.lastName }}</span>
    </div>
    <div>
      <label>Phone Number:</label>
      <span>{{ customer.phoneNumber }}</span>
    </div>
    <div>
      <label>Email Address:</label>
      <span>{{ customer.email }}</span>
    </div>
    <div>
      <label>Postal Code:</label>
      <span>{{ customer.postalCode }}</span>
    </div>
    <div>
      <label>City:</label>
      <span>{{ customer.city }}</span>
    </div>
    <div>
      <label>Street:</label>
      <span>{{ customer.street }}</span>
    </div>
    <div>
      <label>Street Number:</label>
      <span>{{ customer.houseNumber }}</span>
    </div>
    <button @click="goBack">Back</button>
  </div>
  <footerNavigation />
</template>
  
<script>
import axios from '../../axios-auth.js';

export default {
  data() {
    return {
      customer: {
        firstName: "",
        lastName: "",
        phoneNumber: "",
        email: "",
        postalCode: "",
        city: "",
        street: "",
        houseNumber: ""
      }
    };
  },
  mounted() {
    this.getUser();
  },
  methods: {
    getUser() {
      axios
        .get('/users/current', {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("jwt")
          }
        })
        .then((res) => {
          this.customer = res.data;
        })
        .catch(error => console.log(error));
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>
  
<style>
@import '../../assets/css/accountInfo.css';
</style>
  