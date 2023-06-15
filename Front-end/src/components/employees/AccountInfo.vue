<template>
  <headerNavigation />

  <div class="container">
    <h2>Personal Details</h2>
    <div>
      <label>First Name:</label>
      <span v-if="!editMode">{{ customer.firstName }}</span>
      <input type="text" v-else v-model="editedUser.firstName" />
    </div>
    <div>
      <label>Last Name:</label>
      <span v-if="!editMode">{{ customer.lastName }}</span>
      <input type="text" v-else v-model="editedUser.lastName" />
    </div>
    <div>
      <label>Phone Number:</label>
      <span v-if="!editMode">{{ customer.phoneNumber }}</span>
      <input type="text" v-else v-model="editedUser.phoneNumber" />
    </div>
    <div>
      <label>Email Address:</label>
      <span v-if="!editMode">{{ customer.email }}</span>
      <input type="text" v-else v-model="editedUser.email" />
    </div>
    <div>
      <label>Postal Code:</label>
      <span v-if="!editMode">{{ customer.postalCode }}</span>
      <input type="text" v-else v-model="editedUser.postalCode" />
    </div>
    <div>
      <label>City:</label>
      <span v-if="!editMode">{{ customer.city }}</span>
      <input type="text" v-else v-model="editedUser.city" />
    </div>
    <div>
      <label>Street:</label>
      <span v-if="!editMode">{{ customer.street }}</span>
      <input type="text" v-else v-model="editedUser.street" />
    </div>
    <div>
      <label>House Number:</label>
      <span v-if="!editMode">{{ customer.houseNumber }}</span>
      <input type="text" v-else v-model="editedUser.houseNumber" />
    </div>
    <div>
      <label>Status:</label>
      <span v-if="!editMode">{{ customer.active ? 'Active' : 'Inactive' }}</span>
      <select v-else v-model="editedUser.active">
        <option :value="true">Active</option>
        <option :value="false">Inactive</option>
      </select>
    </div>
    <div>
      <button v-if="!editMode" @click="editMode = true">Edit Info</button>
      <button v-if="!editMode" @click="goBack">Back</button>
      <button id="btnUpdate" v-else @click="cancelEdit">Cancel</button>
      <button v-if="editMode" @click="updateInfo">Save Changes</button>
    </div>
  </div>
  <footerNavigation />
</template>

<style>
#btnUpdate {
  margin-right: 150px;
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
  components: {
    headerNavigation,
    footerNavigation
  },
  data() {
    return {
      customer: {
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
        active: true,
        city: ""
      },
      editedUser: {},
      editMode: false
    };
  },
  mounted() {
    const encodedId = this.$route.params.id;
    const decodedId = atob(encodedId);
    this.getUserInfo(decodedId);
  },
  methods: {
    getUserInfo(userId) {
      axios
        .get(`/users/${userId}`, headerToken)
        .then((res) => {
          this.customer = res.data;
          this.editedUser = { ...this.customer }; // Initialize editedUser with customer data
        })
        .catch((error) => console.log(error));
    },
    checkFieldsNotEmpty() {
      if (!this.editedUser.email.includes('@')) {
        alert("Please enter a valid email.");
        return false;
      } else if (!/^\d{10,}$/.test(this.editedUser.phoneNumber)) {
        alert("Phone number has to be at least 10 numbers");
        return false;
      } else if (
        !this.editedUser.firstName ||
        !this.editedUser.lastName ||
        !this.editedUser.postalCode ||
        !this.editedUser.city ||
        !this.editedUser.street ||
        !/^\d+$/.test(this.editedUser.houseNumber)
      ) {
        alert("Please fill all fields before saving changes");
        return false;
      }
      return true;
    },
    updateInfo() {
      if (!this.checkFieldsNotEmpty()) {
        return;
      }
      axios
        .put(`/users/${this.customer.id}`, this.editedUser, headerToken)
        .then((response) => {
          console.log('User information updated successfully!');
          this.customer = { ...this.editedUser }; // Update customer with editedUser data
          this.editMode = false;
          console.log(response);
        })
        .catch((error) => {
          console.error('Error updating user information:', error);
        });
    },
    cancelEdit() {
      this.editMode = false;
      this.editedUser = { ...this.customer }; // Reset editedUser to customer data
    },
    goBack() {
      this.$router.go(-1); // Go back to the previous page
    }
  }
};
</script>

<style>
@import '../../assets/css/accountInfoForEmployee.css';
</style>
