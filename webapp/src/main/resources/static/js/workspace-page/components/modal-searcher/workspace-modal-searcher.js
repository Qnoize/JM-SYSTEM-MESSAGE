import {ChannelRestPaginationService, UserRestPaginationService, WorkspaceRestPaginationService} from '../../../rest/entities-rest-pagination.js'

const channel_service = new ChannelRestPaginationService();
const user_service = new UserRestPaginationService();
const workspace_service = new WorkspaceRestPaginationService();

let available_users;
let available_channels;

window.addEventListener('load', function () {
    const searcher_modal = document.getElementById("modalSearcher");
    const searcher_btn = document.getElementById("buttonSearcher");
    searcher_btn.onclick = function () {
        searcher_modal.style.display = "block";
        const work = workspace_service.getChoosedWorkspace();
        const user = user_service.getLoggedUser();
        Promise.all([work, user]).then(id_values => {
            const load_channels = channel_service.getChannelsByWorkspaceAndUser(id_values[0].id, id_values[1].id);
            const load_users = user_service.getUsersByWorkspace(id_values[0].id);
            Promise.all([load_channels, load_users]).then(values => {
                available_channels = values[0];
                available_users = values[1];
                showSearchResult(available_channels, available_users);
            })
        })

    };
});

function showSearchResult(channels, users) {
    $('#idSearchContent').html('<ol role="listbox">'
        + '<div class="search_channels_in_modal" id="search_channel_in_modal_id">'
        + showChannels(channels)
        + '</div>'
        + '<div class="search_users_in_modal">'
        + showUsers(users)
        + '</div>'
        + '</ol>')
}


function showChannels(channels) {
    return channels.map((channel) => displayItem(channel.id, "channel", channel.name, channelPic(channel))).join("");
}

function showUsers(users) {
    return users.map((user) => displayItem(user.id, "user",user.name + " " + user.lastName, userPic(user))).join("");
}

function channelPic(channel) {
    let pic = "#";
    if (channel.isPrivate) {
        pic = "*";
    }
    return '<i class="searcher__channel_icon_prefix">'
        + pic
        + '</i>';
}

function userPic(user) {
    return '<i class="searcher__channel_icon_prefix">'
        + '@'
        + '</i>';
}

function displayItem(id, itemClass, itemName, pic) {
    return '<li class="search-field-li" data-type="' + itemClass + '" data-id="' + id + '">'
        + '<div class="search-field-name">'
        + pic
        + '<span>'
        + itemName
        + '</span>'
        + '</div>'
        + '</li>';
}

$('#searchInput').bind("change paste keyup", function() {
    const search_str = ($(this).val());
    const searched_channel = available_channels.filter((el) => {
        return el.name.toLowerCase().indexOf(search_str.toLowerCase()) >= 0;
    });
    const searched_users = available_users.filter((el) => {
        const full_name = el.name + " " + el.lastName;
        return full_name.toLowerCase().indexOf(search_str.toLowerCase()) >= 0;
    });
    showSearchResult(searched_channel, searched_users);
});


$("#idSearchContent").on("click", "li.search-field-li", function(){
    const id = $(this).data("id");
    const type = $(this).data("type");
    if (type=="channel") {
        pressChannelButton(id);
        sessionStorage.setItem("channelName", id);
    } else if (type=="user") {
        console.log("Открытие личной переписки с User с id=" + id);
    }
    $("#modalSearcher").modal("hide");
});




