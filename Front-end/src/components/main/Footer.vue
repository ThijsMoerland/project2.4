<template>
  <br><br><br>
  <div id="footer">

    <div class="icons">
      <a href="#top" id="arrowUp">
        <arrowUp />
      </a>
    </div>

    <div id="line"></div>

    <div class="icons">
      <div class="icons">
        <instagram />
      </div>
      <div class="icons">
        <facebook />
      </div>
      <div class="icons">
        <twitter />
      </div>
      <div class="icons">
        <youtube />
      </div>
    </div>

  </div>
</template>
  
<script>
import arrowUp from '../icons/IconArrowUp.vue';
import facebook from '../icons/IconFacebook.vue';
import instagram from '../icons/IconInstagram.vue';
import twitter from '../icons/IconTwitter.vue';
import youtube from '../icons/IconYoutube.vue';

export default {
  name: "footer",
  components: {
    arrowUp,
    facebook,
    instagram,
    twitter,
    youtube
  }
};
</script>


<style>
@import '../../assets/css/footer.css';
</style>