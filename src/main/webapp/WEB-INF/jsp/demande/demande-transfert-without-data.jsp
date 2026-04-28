<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page">
    <div class="header">
        <h1>Nouvelle demande</h1>
        <p>Formulaire statique organise par sections pour preparer une demande de visa avec les champs issus des tables du modele.</p>
        <div class="badge">Type de demande selectionne: ${typeDemande.libelle}</div>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">
            <strong>Erreur:</strong> ${errorMessage}
        </div>
    </c:if>

    <c:if test="${not empty succesMessage}">
        <div class="alert alert-success">
            <strong>Succes:</strong> ${succesMessage}
        </div>
    </c:if>

    <div class="form-card">
        <form action="/demande/transfert-sans-donnee" method="post">
            <div class="form-grid">
                <details class="section">
                    <summary>
                        <span>Etat civil</span>
                        <span class="summary-note">Identite et informations personnelles</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="nom">Nom <span class="required-mark">*</span></label>
                                <input id="nom" name="nom" type="text" placeholder="Nom" required>
                            </div>
                            <div class="field col-6">
                                <label for="prenom">Prenom</label>
                                <input id="prenom" name="prenom" type="text" placeholder="Prenom">
                            </div>
                            <div class="field col-6">
                                <label for="nomJeuneFille">Nom de jeune fille</label>
                                <input id="nomJeuneFille" name="nomJeuneFille" type="text" placeholder="Nom de jeune fille">
                            </div>
                            <div class="field col-6">
                                <label for="email">Email</label>
                                <input id="email" name="email" type="email" placeholder="exemple@mail.com">
                            </div>
                            <div class="field col-4">
                                <label for="dateNaissance">Date de naissance <span class="required-mark">*</span></label>
                                <input id="dateNaissance" name="dateNaissance" type="date" required>
                            </div>
                            <div class="field col-4">
                                <label for="lieuNaissance">Lieu de naissance</label>
                                <input id="lieuNaissance" name="lieuNaissance" type="text" placeholder="Lieu de naissance">
                            </div>
                            <div class="field col-4">
                                <label for="telephone">Telephone <span class="required-mark">*</span></label>
                                <input id="telephone" name="telephone" type="tel" placeholder="Telephone" required>
                            </div>
                            <div class="field full-width">
                                <label for="adresse">Adresse <span class="required-mark">*</span></label>
                                <textarea id="adresse" name="adresse" placeholder="Adresse complete" required></textarea>
                            </div>
                            <div class="field col-6">
                                <label for="nationalite">Nationalite <span class="required-mark">*</span></label>
                                <select id="nationalite" name="nationalite" required>
                                    <option value="">Selectionner une nationalite</option>
                                    <c:forEach items="${nationalites}" var="nationalite">
                                        <option value="${nationalite.id}">${nationalite.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="field col-6">
                                <label for="situationFamiliale">Situation familiale <span class="required-mark">*</span></label>
                                <select id="situationFamiliale" name="situationFamiliale" required>
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
                                <label for="numeroPasseport">Numero passeport <span class="required-mark">*</span></label>
                                <input id="numeroPasseport" name="numeroPasseport" type="text" placeholder="Numero du passeport" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationPasseport">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpirationPasseport" name="dateExpirationPasseport" type="date" required>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section">
                    <summary>
                        <span>Ancien Passeport</span>
                        <span class="summary-note">Identifiant de voyage</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroPasseport">Numero passeport <span class="required-mark">*</span></label>
                                <input id="numeroPasseport" name="numeroPasseportAncien" type="text" placeholder="Numero du passeport" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpirationPasseport">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpirationPasseport" name="dateExpirationPasseportAncien" type="date" required>
                            </div>
                        </div>
                    </div>
                </details>

                <details class="section">
                    <summary>
                        <span>Visa</span>
                        <span class="summary-note">Visa en cours de transformation</span>
                    </summary>
                    <div class="section-body">
                        <div class="section-hint">Tous les champs de cette section sont obligatoires.</div>
                        <div class="section-grid">
                            <div class="field col-6">
                                <label for="numeroVisaTransformable">Numero visa <span class="required-mark">*</span></label>
                                <input id="numeroVisaTransformable" name="numeroVisaAncien" type="text" placeholder="Numero du visa transformable" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateEntre">Date d'entre <span class="required-mark">*</span></label>
                                <input id="dateEntre" name="dateEntre" type="date" required>
                            </div>
                            <div class="field col-6">
                                <label for="dateExpiration">Date d'expiration <span class="required-mark">*</span></label>
                                <input id="dateExpiration" name="dateExpiration" type="date" required>
                            </div>
                                <div class="field col-6">
                                <label for="dateDelivrance">Date de delivrance <span class="required-mark">*</span></label>
                                <input id="dateDelivrance" name="dateDelivrance" type="date" required>
                            </div>

                            <div class="field col-6">
                            <label for="pays">Pays d'entree <span class="required-mark">*</span></label>
                                <select id="pays" name="idPaysEntre" required>
                                    <option value="">Selectionner un pays d'entree</option>
                                    <c:forEach items="${pays}" var="pay">
                                        <option value="${pay.id}">${pay.libelle}</option>
                                    </c:forEach>
                                </select>
                            </div>

                        <div class="field col-6">
                                <label for="typeVisa">Type visa <span class="required-mark">*</span></label>
                                <select id="typeVisa" name="idTypeVisa" required>
                                    <option value="">Selectionner un type visa</option>
                                    <c:forEach items="${typesVisa}" var="typeVisa">
                                        <option value="${typeVisa.id}">${typeVisa.libelle}</option>
                                    </c:forEach>
                                </select>
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
</main>
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