package org.jtwig.render.node.renderer;

import com.google.common.base.Optional;
import org.jtwig.model.tree.BlockNode;
import org.jtwig.model.tree.Node;
import org.jtwig.render.RenderRequest;
import org.jtwig.render.context.model.BlockDefinition;
import org.jtwig.renderable.Renderable;
import org.jtwig.resource.reference.ResourceReference;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class BlockNodeRenderTest {
    private BlockNodeRender underTest = new BlockNodeRender();

    @Test
    public void render() throws Exception {
        String identifier = "identifier";
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        Renderable renderable = mock(Renderable.class);
        BlockNode node = mock(BlockNode.class);
        Node content = mock(Node.class);
        ResourceReference reference = mock(ResourceReference.class);

        when(node.getIdentifier()).thenReturn(identifier);
        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(reference);
        when(request.getRenderContext().getBlockContext().getCurrent().get(identifier))
                .thenReturn(Optional.of(new BlockDefinition(content, reference)));
        when(request.getEnvironment().getRenderEnvironment().getRenderNodeService().render(request, content))
                .thenReturn(renderable);

        Renderable result = underTest.render(request, node);

        assertSame(renderable, result);
        verify(request.getRenderContext().getBlockContext().getCurrent()).add(node, reference);
    }
}