package jm.model.message;

import jm.model.Bot;
import jm.model.Channel;
import jm.model.ThreadChannel;
import jm.model.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString
@Entity
@Table(name = "thread_channel_messages")
public class ThreadChannelMessage extends Message {

    @ManyToOne
    private ThreadChannel threadChannel;
}
