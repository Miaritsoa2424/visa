<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Demande confirmée</title>
    <style>
        :root {
            --bg: #eef2f7;
            --card: #ffffff;
            --text: #1f2937;
            --muted: #6b7280;
            --border: #d7dde6;
            --accent: #2563eb;
            --success: #16a34a;
            --success-soft: #dcfce7;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: linear-gradient(180deg, #eef2f7 0%, #f7f9fc 100%);
            color: var(--text);
        }

        .page {
            max-width: 1180px;
            margin: 0 auto;
            padding: 32px 20px 48px;
        }

        .hero {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 18px;
            padding: 28px;
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.06);
            margin-bottom: 20px;
        }

        .hero h1 {
            margin: 0 0 8px;
            font-size: 32px;
        }

        .hero p {
            margin: 0;
            color: var(--muted);
            line-height: 1.6;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-top: 16px;
            padding: 8px 12px;
            border-radius: 999px;
            background: var(--success-soft);
            color: var(--success);
            font-weight: 700;
            font-size: 13px;
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(12, minmax(0, 1fr));
            gap: 18px;
        }

        .card {
            grid-column: span 12;
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 18px;
            padding: 24px;
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.06);
        }

        .card h2 {
            margin: 0 0 16px;
            font-size: 20px;
        }

        .summary {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));
            gap: 14px 18px;
        }

        .item {
            padding: 14px 16px;
            border-radius: 14px;
            background: #f9fbfd;
            border: 1px solid #e6ebf2;
        }

        .label {
            display: block;
            font-size: 12px;
            color: var(--muted);
            margin-bottom: 6px;
            text-transform: uppercase;
            letter-spacing: 0.04em;
        }

        .value {
            font-size: 15px;
            font-weight: 700;
            color: var(--text);
            word-break: break-word;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 22px;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 18px;
            border-radius: 12px;
            border: 1px solid transparent;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
        }

        .btn-primary {
            background: var(--accent);
            color: #fff;
        }

        .btn-secondary {
            background: #fff;
            color: var(--text);
            border-color: var(--border);
        }

        @media (max-width: 900px) {
            .summary {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>
<main class="app-main">
<div class="page">
    <div class="hero">
        <h1>Demande enregistrée avec succès</h1>
        <p>La demande a été insérée dans la base de données. Un statut initial <strong>${statutInitial}</strong> a également été créé automatiquement.</p>
        <div class="badge">Insertion confirmée</div>
    </div>

    <div class="grid">
        <div class="card">
            <h2>Informations de la demande</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">ID demande</span>
                    <span class="value">${demande.id}</span>
                </div>
                <div class="item">
                    <span class="label">Date demande</span>
                    <span class="value">${demande.dateDemande}</span>
                </div>
                <div class="item">
                    <span class="label">Type visa</span>
                    <span class="value">${demande.typeVisa.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Type demande</span>
                    <span class="value">${demande.typeDemande.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Statut initial</span>
                    <span class="value">${statutInitial}</span>
                </div>
                <div class="item">
                    <span class="label">Champs de dossier enregistrés</span>
                    <span class="value">${champFournirCount}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>État civil</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Nom</span>
                    <span class="value">${dto.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Prénom</span>
                    <span class="value">${dto.prenom}</span>
                </div>
                <div class="item">
                    <span class="label">Date de naissance</span>
                    <span class="value">${dto.dateNaissance}</span>
                </div>
                <div class="item">
                    <span class="label">Téléphone</span>
                    <span class="value">${dto.telephone}</span>
                </div>
                <div class="item">
                    <span class="label">Nationalité</span>
                    <span class="value">${demande.passeport.personne.nationalite.libelle}</span>
                </div>
                <div class="item">
                    <span class="label">Situation familiale</span>
                    <span class="value">${demande.passeport.personne.situationFamiliale.libelle}</span>
                </div>
                <div class="item" style="grid-column: span 2;">
                    <span class="label">Adresse</span>
                    <span class="value">${dto.adresse}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Passeport</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numéro passeport</span>
                    <span class="value">${dto.numeroPasseport}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${dto.dateExpirationPasseport}</span>
                </div>
                <div class="item">
                    <span class="label">Demandeur</span>
                    <span class="value">${demande.passeport.personne.prenom} ${demande.passeport.personne.nom}</span>
                </div>
                <div class="item">
                    <span class="label">Email</span>
                    <span class="value">${dto.email}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Visa transformable</h2>
            <div class="summary">
                <div class="item">
                    <span class="label">Numéro</span>
                    <span class="value">${dto.numeroVisaTransformable}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'entrée</span>
                    <span class="value">${dto.dateArrivee}</span>
                </div>
                <div class="item">
                    <span class="label">Date d'expiration</span>
                    <span class="value">${dto.dateExpirationVisaTransformable}</span>
                </div>
            </div>
        </div>

        <div class="card">
            <h2>Action</h2>
            <div class="actions">
                <a class="btn btn-primary" href="/demandes">Voir la liste des demandes</a>
                <a class="btn btn-secondary" href="/demande/nouvelle?typeDemandeId=${demande.typeDemande.id}">Créer une autre demande</a>
            </div>
        </div>
    </div>
</div>
</main>
</body>
</html>
