<!DOCTYPE html>
<html>
 <head>
   <title>RECOMMEND</title>
 </head>
 <body>
   <h1>Recommend</h1>
  <p>This is a song recommender</p>

<iframe name="hiddenFrame" width="0" height="0" border="0" style="display: none;"></iframe>
 <form action="/demo/add" target="hiddenFrame" method="get">
   Input Song name:<br>
   <input type="text" name="song"><br>
   Input Artist name:<br>
   <input type="text" name="artist"><br>

   <select name= "genre">
     <option value="Pop">Pop</option>
     <option value="Hiphop">Hip Hop</option>
     <option value="Country">Country</option>
     <option value="Chill">Chill</option>
     <option value="Dance/Electronic">Dance/Electronic</option>
     <option value="Rock">Rock</option>
     <option value="Soul">Soul</option>
     <option value="Indie">Indie</option>
     <option value="K-pop">K-pop</option>
   </select>

   <input type="submit" value="Submit">
 </form>
 <form action="/demo/all">
   <input type="submit" value="ViewDB">
 </form>
</form>
<form action="/demo/request">
  <input type="submit" value="Recommend">
</form>


</body>
</html>
