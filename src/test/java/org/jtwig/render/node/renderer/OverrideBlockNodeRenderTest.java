package org.jtwig.render.node.renderer;

import org.jtwig.model.tree.OverrideBlockNode;
import org.jtwig.render.RenderRequest;
import org.jtwig.renderable.Renderable;
import org.jtwig.renderable.impl.EmptyRenderable;
import org.jtwig.resource.reference.ResourceReference;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class OverrideBlockNodeRenderTest {
    private final OverrideBlockNodeRender underTest = new OverrideBlockNodeRender();

    @Test
    public void render() throws Exception {
        RenderRequest request = mock(RenderRequest.class, RETURNS_DEEP_STUBS);
        OverrideBlockNode blockNode = mock(OverrideBlockNode.class);
        ResourceReference resourceReference = mock(ResourceReference.class);

        when(request.getRenderContext().getResourceContext().getCurrent()).thenReturn(resourceReference);

        Renderable result = underTest.render(request, blockNode);

        verify(request.getRenderContext().getBlockContext().getCurrent()).add(blockNode, resourceReference);
        assertSame(EmptyRenderable.instance(), result);
    }
}