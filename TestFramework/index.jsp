<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test framework fromulaire</title>
</head>
<body>
    <h2>fromulaire d'enregistrement</h2>
    <form action="save.do" method="POST">
        <input type="number" name="id" placeholder="id">
        <input type="text" name="nom" placeholder="Entrer le nom">
        <input type="submit" value="Valider">
    </form>
    <hr>

    <h2>fromulaire d'enregistrement2 </h2>
    <form action="setNewTest.do" method="POST">
        <input type="number" name="identifiant" placeholder="id">
        <input type="text" name="name" placeholder="Entrer le nom">
        <input type="submit" value="Valider">
    </form>
    <hr>

    <h2>fromulaire upload </h2>
    <form action="upload.do" method="POST" enctype="multipart/form-data">
        <input type="file" name="fu">
        <input type="submit" value="Valider">
    </form>
    
</body>
</html>