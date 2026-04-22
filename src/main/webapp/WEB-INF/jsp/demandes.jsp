<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        .container {
            max-width: 1100px;
            margin: 50px auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #333;
            text-align: center;
            margin-top: 0;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            margin: 5px 0 15px;
        }

        .btn-primary {
            background-color: #4CAF50;
            color: white;
        }

        .btn-primary:hover {
            background-color: #45a049;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:hover {
            background-color: #f5f5f5;
        }

        .empty-message {
            text-align: center;
            padding: 20px;
            color: #666;
            background-color: #fafafa;
            border: 1px solid #e3e3e3;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>
<main class="app-main">
<div class="container">
    <h1>Liste des demandes</h1>
    <a class="btn btn-primary" href="/home">Retour a l'accueil</a>

    <c:choose>
        <c:when test="${not empty demandes}">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Date demande</th>
                    <th>Demandeur</th>
                    <th>Numéro passeport</th>
                    <th>Type visa</th>
                    <th>Type demande</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${demandes}" var="demande">
                    <tr>
                        <td>${demande.id}</td>
                        <td>${demande.dateDemande}</td>
                        <td>${demande.passeport.personne.prenom} ${demande.passeport.personne.nom}</td>
                        <td>${demande.passeport.numero}</td>
                        <td>${demande.typeVisa.libelle}</td>
                        <td>${demande.typeDemande.libelle}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <div class="empty-message">Aucune demande trouvee.</div>
        </c:otherwise>
    </c:choose>
</div>
</main>
</body>
</html>