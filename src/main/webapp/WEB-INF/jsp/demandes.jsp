<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des demandes</title>
    <style>
        :root {
            --bg: #eef2f7;
            --card: #ffffff;
            --text: #1f2937;
            --muted: #64748b;
            --border: #d9e2ec;
            --accent: #3b82f6;
            --accent-soft: #dbeafe;
        }

        body {
            font-family: Arial, Helvetica, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(180deg, var(--bg) 0%, #f7f9fc 100%);
        }

        .container {
            max-width: 1100px;
            margin: 50px auto;
            background-color: var(--card);
            padding: 30px;
            border-radius: 18px;
            border: 1px solid var(--border);
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.07);
        }

        h1 {
            color: var(--text);
            margin-top: 0;
            margin-bottom: 8px;
            font-size: 30px;
        }

        .subtitle {
            margin: 0 0 18px;
            color: var(--muted);
        }

        .btn {
            padding: 11px 16px;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
            margin: 5px 0 15px;
            font-weight: 700;
        }

        .btn-primary {
            background-color: var(--accent);
            color: white;
            box-shadow: 0 10px 20px rgba(59, 130, 246, 0.24);
        }

        .btn-primary:hover {
            background-color: #2563eb;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
            border-radius: 14px;
            overflow: hidden;
            border: 1px solid var(--border);
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid var(--border);
        }

        th {
            background: linear-gradient(90deg, #eff6ff 0%, #f8fbff 100%);
            color: #1e3a8a;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.03em;
        }

        tr:hover {
            background-color: #f8fbff;
        }

        .empty-message {
            text-align: center;
            padding: 20px;
            color: var(--muted);
            background-color: #fafcff;
            border: 1px solid var(--border);
            border-radius: 12px;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>
<main class="app-main">
<div class="container">
    <h1>Liste des demandes</h1>
    <p class="subtitle">Consultez rapidement toutes les demandes saisies dans le systeme.</p>
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