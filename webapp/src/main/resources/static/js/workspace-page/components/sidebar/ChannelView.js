import {ChannelRestPaginationService} from "/js/rest/entities-rest-pagination.js";
import {updateAllMessages} from "/js/workspace-page/components/footer/messages.js";

export class ChannelView {
    default_channel = null;

    constructor() {
        this.channel_service = new ChannelRestPaginationService();
    }

    setLoggedUser(loggedUser) {
        this.loggedUser = loggedUser;
        return this;
    }

    showAllChannels(workspace_id) {
        this.setLocalStorageSettings(0);
        this.channel_service.getChannelsByWorkspaceId(workspace_id).then(
            channels => {
                if (channels.length > 0) {
                    this.addChannels(channels);
                    if (this.default_channel !== null) {
                        this.setLocalStorageSettings(this.default_channel.id);
                        this.setChannelBGColor(this.default_channel);
                        updateAllMessages();
                    }
                }
            }
        );
    }

    addChannels(channels) {
        $.each(channels, (idx, chn) => {
            if (!chn.isArchived && this.checkPrivacy(chn)) {
                if (this.default_channel === null) {
                    this.default_channel = chn;
                }
                this.addChannelIntoSidebarChannelList(chn);
            }
        });
    }

    addChannelIntoSidebarChannelList(channel) {
        const chn_symbol = channel.isPrivate ? "🔒" : "#";
        $('#id-channel_sidebar__channels__list').append(`
            <div class="p-channel_sidebar__channel">
                <button class="p-channel_sidebar__name_button" id="channel_button_${channel.id}" value="${channel.id}">
                    <i class="p-channel_sidebar__channel_icon_prefix">${chn_symbol}</i>
                    <span class="p-channel_sidebar__name-3" id="channel_name_${channel.id}">${channel.name}</span>
                </button>
            </div>`);
    }

    setChannelBGColor(channel) {
        $(`#channel_button_${channel.id}`).css({
            color: 'white',
            background: 'royalblue'
        });
        $(".p-classic_nav__model__title__info__name").html("").text(channel.name);
    }

    setLocalStorageSettings(chn_id) {
        sessionStorage.setItem('channelName', chn_id);
        sessionStorage.setItem('conversation_id', '0');
        window.channel_id = chn_id;
    }

    checkPrivacy(channel) {
        return (channel.isPrivate && this.loggedUser === channel.ownerId) || !channel.isPrivate;
    }
}