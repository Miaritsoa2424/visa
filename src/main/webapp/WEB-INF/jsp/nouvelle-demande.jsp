<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nouvelle demande</title>
    <style>
        :root {
            --bg: #eef2f7;
            --card: #ffffff;
            --text: #1f2937;
            --muted: #6b7280;
            --border: #d7dde6;
            --accent: #2563eb;
            --accent-soft: #dbeafe;
            --field-bg: #f9fbfd;
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

        .header {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 18px;
            padding: 24px 28px;
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.06);
            margin-bottom: 20px;
        }

        .header h1 {
            margin: 0 0 8px;
            font-size: 30px;
        }

        .header p {
            margin: 0;
            color: var(--muted);
            line-height: 1.5;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-top: 16px;
            padding: 8px 12px;
            border-radius: 999px;
            background: var(--accent-soft);
            color: var(--accent);
            font-weight: 700;
            font-size: 13px;
        }

        .form-card {
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 18px;
            padding: 24px;
            box-shadow: 0 12px 30px rgba(15, 23, 42, 0.06);
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(12, minmax(0, 1fr));
            gap: 18px;
        }

        details.section {
            grid-column: span 12;
            border: 1px solid var(--border);
            border-radius: 16px;
            background: #fff;
            overflow: hidden;
        }

        details.section[open] {
            box-shadow: inset 0 0 0 1px rgba(37, 99, 235, 0.12);
        }

        summary {
            list-style: none;
            cursor: pointer;
            padding: 18px 20px;
            background: linear-gradient(90deg, #f8fbff 0%, #ffffff 100%);
            font-size: 18px;
            font-weight: 700;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            user-select: none;
        }

        summary::after {
            content: "▾";
            color: var(--accent);
            font-size: 18px;
            font-weight: 700;
            transition: transform 0.2s ease;
        }

        details.section[open] summary::after {
            transform: rotate(180deg);
        }

        summary::-webkit-details-marker {
            display: none;
        }

        .summary-note {
            font-size: 12px;
            color: var(--muted);
            font-weight: 600;
        }

        .section-body {
            padding: 20px;
            border-top: 1px solid var(--border);
            background: #fff;
        }

        .section-grid {
            display: grid;
            grid-template-columns: repeat(12, minmax(0, 1fr));
            gap: 16px;
        }

        .field {
            grid-column: span 12;
        }

        .field.col-6 {
            grid-column: span 6;
        }

        .field.col-4 {
            grid-column: span 4;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 700;
            font-size: 14px;
        }

        input,
        select,
        textarea {
            width: 100%;
            padding: 12px 14px;
            border: 1px solid var(--border);
            border-radius: 12px;
            background: var(--field-bg);
            color: var(--text);
            font-size: 14px;
            outline: none;
        }

        textarea {
            min-height: 110px;
            resize: vertical;
        }

        input:focus,
        select:focus,
        textarea:focus {
            border-color: var(--accent);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
            background: #fff;
        }

        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 12px;
            margin-top: 22px;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 150px;
            padding: 12px 18px;
            border-radius: 12px;
            border: 1px solid transparent;
            font-weight: 700;
            text-decoration: none;
            cursor: pointer;
            transition: transform 0.15s ease, box-shadow 0.15s ease;
        }

        .btn:hover {
            transform: translateY(-1px);
        }

        .btn-primary {
            background: var(--accent);
            color: #fff;
            box-shadow: 0 10px 20px rgba(37, 99, 235, 0.22);
        }

        .btn-secondary {
            background: #fff;
            color: var(--text);
            border-color: var(--border);
        }

        .full-width {
            grid-column: span 12;
        }

        .dynamic-champs {
            grid-column: span 12;
            border: 1px dashed var(--border);
            border-radius: 12px;
            background: #fcfdff;
            padding: 14px;
        }

        .dynamic-champs h4 {
            margin: 0 0 10px;
            font-size: 14px;
            color: var(--text);
        }

        .checkbox-list {
            display: grid;
            gap: 10px;
        }

        .checkbox-item {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 14px;
            color: var(--text);
        }

        .empty-dynamic {
            margin: 0;
            color: var(--muted);
            font-size: 13px;
        }

        @media (max-width: 900px) {
            .field.col-6,
            .field.col-4 {
                grid-column: span 12;
            }

            .form-actions {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<div class="page">
    <div class="header">
        <h1>Nouvelle demande</h1>
        <p>Formulaire statique organise par sections pour preparer une demande de visa avec les champs issus des tables du modele.</p>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <div class="form-card">
        <form action="/demande/creer" method="post">
            <div class="form-grid">
                <details class="section">
                    <summary>
                        <span>Etat civil</span>
                        <span class="summary-note">Identite et informations personnelles</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="nom">Nom</label>
                                <input id="nom" name="nom" type="text" placeholder="Nom" value="Ndiaye">
                            </div>
                            <div class="field col-6">
                                <label for="prenom">Prenom</label>
                                <input id="prenom" name="prenom" type="text" placeholder="Prenom" value="Aminata">
                            </div>
                            <div class="field col-6">
                                <label for="nomJeuneFille">Nom de jeune fille</label>
                                <input id="nomJeuneFille" name="nomJeuneFille" type="text" placeholder="Nom de jeune fille" value="Traore">
                            </div>
                            <div class="field col-6">
                                <label for="email">Email</label>
                                <input id="email" name="email" type="email" placeholder="exemple@mail.com" value="aminata.ndiaye@example.com">
                            </div>
                            <div class="field col-4">
                                <label for="dateNaissance">Date de naissance</label>
                                <input id="dateNaissance" name="dateNaissance" type="date" value="1998-05-14">
                            </div>
                            <div class="field col-4">
                                <label for="lieuNaissance">Lieu de naissance</label>
                                <input id="lieuNaissance" name="lieuNaissance" type="text" placeholder="Lieu de naissance" value="Dakar">
                            </div>
                            <div class="field col-4">
                                <label for="telephone">Telephone</label>
                                <input id="telephone" name="telephone" type="tel" placeholder="Telephone" value="770000000">
                            </div>
                            <div class="field full-width">
                                <label for="adresse">Adresse</label>
                                <textarea id="adresse" name="adresse" placeholder="Adresse complete">Avenue Cheikh Anta Diop, Dakar</textarea>
                            </div>
                            <div class="field col-6">
                                <label for="nationalite">Nationalite</label>
                                <select id="nationalite" name="nationalite">
                                    <option value="">Selectionner une nationalite</option>
                                    <c:forEach items="${nationalites}" var="nationalite">
                                        <option value="${nationalite.id}">${nationalite.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="field col-6">
                                <label for="situationFamiliale">Situation familiale</label>
                                <select id="situationFamiliale" name="situationFamiliale">
                                    <option value="">Selectionner une situation</option>
                                    <c:forEach items="${situationsFamiliales}" var="situation">
                                        <option value="${situation.id}">${situation.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section">
                    <summary>
                        <span>Passeport</span>
                        <span class="summary-note">Identifiant de voyage</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroPasseport">Numero passeport</label>
                                <input id="numeroPasseport" name="numeroPasseport" type="text" placeholder="Numero du passeport" value="SN12345678">
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationPasseport">Date d'expiration</label>
                                <input id="dateExpirationPasseport" name="dateExpirationPasseport" type="date" value="2030-12-31">
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section">
                    <summary>
                        <span>Visa transformable</span>
                        <span class="summary-note">Visa en cours de transformation</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroVisaTransformable">Numero visa transformable</label>
                                <input id="numeroVisaTransformable" name="numeroVisaTransformable" type="text" placeholder="Numero du visa transformable" value="VISA-TR-001">
                            </div>
                            <div class="field col-6">
                                <label for="dateArrivee">Date d'arrivee</label>
                                <input id="dateArrivee" name="dateArrivee" type="date" value="2026-04-21">
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationVisaTransformable">Date d'expiration</label>
                                <input id="dateExpirationVisaTransformable" name="dateExpirationVisaTransformable" type="date" value="2027-04-21">
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section">
                    <summary>
                        <span>Visa demande</span>
                        <span class="summary-note">Demande principale</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-4">
                                <label for="dateDemande">Date demande</label>
                                <input id="dateDemande" name="dateDemande" type="date" value="2026-04-21">
                            </div>
                            <div class="field col-4">
                                <label for="typeVisa">Type visa</label>
                                <select id="typeVisa" name="typeVisa">
                                    <option value="">Selectionner un type visa</option>
                                    <c:forEach items="${typesVisa}" var="typeVisa">
                                        <option value="${typeVisa.id}">${typeVisa.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="field col-4">
                                <input id="typeDemandeId" name="typeDemandeId" type="hidden" value="${not empty typeDemandeId ? typeDemandeId : 1}">
                            </div>

                            <div class="dynamic-champs">
                                <h4>Champs a fournir (selon le type de visa)</h4>
                                <div id="champsFournirContainer" class="checkbox-list">
                                    <p class="empty-dynamic">Selectionnez un type de visa pour afficher les champs.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </details>
            </div>

            <div class="form-actions">
                <a class="btn btn-secondary" href="/demandes">Annuler</a>
                <button class="btn btn-primary" type="submit">Previsualiser la demande</button>
            </div>
        </form>
    </div>
</div>
<script>
    (function () {
        const typeVisaSelect = document.getElementById("typeVisa");
        const container = document.getElementById("champsFournirContainer");

        async function loadChampsFournir(typeVisaId) {
            if (!typeVisaId) {
                container.innerHTML = '<p class="empty-dynamic">Selectionnez un type de visa pour afficher les champs.</p>';
                return;
            }

            try {
                const url = "/demande/champs-fournir?typeVisaId=" + encodeURIComponent(typeVisaId);
                const response = await fetch(url);
                if (!response.ok) {
                    throw new Error("Erreur de chargement des champs a fournir");
                }

                const champs = await response.json();
                if (!Array.isArray(champs) || champs.length === 0) {
                    container.innerHTML = '<p class="empty-dynamic">Aucun champ requis pour ce type de visa.</p>';
                    return;
                }

                const html = champs
                    .map(function (champ) {
                        return '<label class="checkbox-item" for="champ-' + champ.id + '">' +
                            '<input id="champ-' + champ.id + '" type="checkbox" name="champFournirIds" value="' + champ.id + '">' +
                            '<span>' + champ.libelle + '</span>' +
                            '</label>';
                    })
                    .join("");

                container.innerHTML = html;
            } catch (error) {
                container.innerHTML = '<p class="empty-dynamic">Impossible de charger les champs. Reessayez.</p>';
            }
        }

        typeVisaSelect.addEventListener("change", function (event) {
            loadChampsFournir(event.target.value);
        });

        loadChampsFournir(typeVisaSelect.value);
    })();
</script>
</body>
</html>