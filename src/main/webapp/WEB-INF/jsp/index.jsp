<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visa - Bienvenue</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 900px;
            margin: 50px auto;
            padding: 20px;
            background-color: #f4f4f4;
        }

        .container {
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

        .list-title {
            margin: 20px 0 10px;
            color: #333;
        }

        .type-list {
            list-style: none;
            margin: 0;
            padding: 0;
            border: 1px solid #ddd;
            border-radius: 6px;
            overflow: hidden;
        }

        .type-list li {
            border-bottom: 1px solid #ddd;
        }

        .type-list li:last-child {
            border-bottom: none;
        }

        .type-link {
            display: block;
            padding: 14px 16px;
            text-decoration: none;
            color: #333;
            background-color: #fff;
            font-weight: 600;
        }

        .type-link:hover {
            background-color: #f5f5f5;
            color: #1976D2;
        }

        .empty-message {
            text-align: center;
            padding: 20px;
            color: #666;
            background-color: #fafafa;
            border: 1px solid #e3e3e3;
            border-radius: 4px;
        }

        .hint {
            color: #666;
            margin-top: 8px;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Bienvenue</h1>
    <p>Choisissez le type de demande que vous voulez faire.</p>

    <a class="btn btn-primary" href="/demandes">Voir la liste des demandes</a>

    <h2 class="list-title">Types de demande</h2>
    <c:choose>
        <c:when test="${not empty typesDemande}">
            <ul class="type-list">
                <c:forEach items="${typesDemande}" var="typeDemande">
                    <li>
                        <a class="type-link" href="/demande/nouvelle?typeDemandeId=${typeDemande.id}">${typeDemande.libelle}</a>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <div class="empty-message">Aucun type de demande n'est disponible pour le moment.</div>
            <p class="hint">Verifiez que la table type_demande contient des donnees, puis rechargez /home.</p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
