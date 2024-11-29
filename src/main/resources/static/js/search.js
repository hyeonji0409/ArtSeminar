let queryType = '[[${queryType}]]'
let pageNumber = '[[${pagination.getNumber()}]]';
pageNumber = parseInt(pageNumber)
const search =  document.querySelector("#search");
const form = document.querySelector("form");
form.querySelector(`option[value=${queryType}]`).selected = true;

const prePage = document.querySelector("#previous-page");
const postPage = document.querySelector("#next-page");



form.onsubmit = (e) => {
    e.preventDefault();
    // if (pageNumber==0) pageNumber+=1
    window.location = `/notice?page=1&qt=${new FormData(form).get("qt")}&q=${search.value}`
}
prePage.onclick = (e) => {
    // form.submit()
    window.location = `/notice?page=${pageNumber}&qt=${queryType}&q=${search.value}`
}
postPage.onclick = (e) => {
    // form.submit()
    pageNumber+=2
    window.location = `/notice?page=${pageNumber}&qt=${queryType}&q=${search.value}`
}