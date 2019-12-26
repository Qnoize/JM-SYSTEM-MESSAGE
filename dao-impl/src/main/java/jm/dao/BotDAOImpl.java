
package jm.dao;

import jm.api.dao.BotDAO;
import jm.model.Bot;
import jm.model.Channel;
import jm.model.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class BotDAOImpl extends AbstractDao<Bot> implements BotDAO {
    private static final Logger logger = LoggerFactory.getLogger(BotDAOImpl.class);

    @Override
    public Optional<Bot> getBotByWorkspaceId(Workspace workspace) {
        return Optional.ofNullable((Bot) entityManager.createNativeQuery("SELECT * FROM bots WHERE workspace_id=?", Bot.class)
                    .setParameter(1, workspace)
                    .getSingleResult());
    }

    @Override
    public Set<Channel> getChannels(Bot bot) { return bot.getChannels(); }
}
