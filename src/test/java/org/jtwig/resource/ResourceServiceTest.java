package org.jtwig.resource;

import com.google.common.base.Optional;
import org.jtwig.resource.exceptions.ResourceException;
import org.jtwig.resource.loader.ResourceLoader;
import org.jtwig.resource.loader.TypedResourceLoader;
import org.jtwig.resource.reference.ResourceReference;
import org.jtwig.resource.reference.ResourceReferenceExtractor;
import org.jtwig.resource.resolver.RelativeResourceResolver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceServiceTest {
    private final HashMap<String, ResourceLoader> resourceLoaderMap = new HashMap<>();
    private final List<TypedResourceLoader> resourceLoaderList = new ArrayList<>();
    private final ArrayList<String> absoluteResourceTypes = new ArrayList<>();
    private final ArrayList<RelativeResourceResolver> relativeResourceResolvers = new ArrayList<>();
    private final ResourceReferenceExtractor resourceReferenceExtractor = mock(ResourceReferenceExtractor.class);

    private final ResourceService underTest = new ResourceService(resourceLoaderMap, resourceLoaderList, absoluteResourceTypes, relativeResourceResolvers, resourceReferenceExtractor);

    @Before
    public void setUp() throws Exception {
        resourceLoaderMap.clear();
        resourceLoaderList.clear();
        absoluteResourceTypes.clear();
        relativeResourceResolvers.clear();
    }

    @Test
    public void resolveAbsoluteSource() throws Exception {
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);

        absoluteResourceTypes.add("blah");
        when(source.getType()).thenReturn("blah");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(reference, result);
    }

    @Test
    public void resolveAbsoluteTarget() throws Exception {
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);

        absoluteResourceTypes.add("blah");
        when(source.getType()).thenReturn("tu");
        when(reference.getType()).thenReturn("blah");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(reference, result);
    }

    @Test
    public void resolveSameTypeNonAbsolute() throws Exception {
        ResourceReference expected = mock(ResourceReference.class);
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);
        RelativeResourceResolver relativeResourceResolver = mock(RelativeResourceResolver.class);

        relativeResourceResolvers.add(relativeResourceResolver);
        when(source.getType()).thenReturn("blah");
        when(reference.getType()).thenReturn("blah");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);
        when(relativeResourceResolver.resolve(source, reference)).thenReturn(Optional.of(expected));

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(expected, result);
    }

    @Test
    public void resolveAnyTargetNonAbsolute() throws Exception {
        ResourceReference expected = mock(ResourceReference.class);
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);
        RelativeResourceResolver relativeResourceResolver = mock(RelativeResourceResolver.class);

        relativeResourceResolvers.add(relativeResourceResolver);
        when(source.getType()).thenReturn("blah");
        when(reference.getType()).thenReturn("any");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);
        when(relativeResourceResolver.resolve(source, reference)).thenReturn(Optional.of(expected));

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(expected, result);
    }

    @Test
    public void resolveSameTypeNonAbsoluteNotFound() throws Exception {
        ResourceReference expected = mock(ResourceReference.class);
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);
        RelativeResourceResolver relativeResourceResolver = mock(RelativeResourceResolver.class);

        relativeResourceResolvers.add(relativeResourceResolver);
        when(source.getType()).thenReturn("blah");
        when(reference.getType()).thenReturn("blah");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);
        when(relativeResourceResolver.resolve(source, reference)).thenReturn(Optional.<ResourceReference>absent());

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(reference, result);
    }

    @Test
    public void resolveRelativeSourceAnyTarget() throws Exception {
        ResourceReference expected = mock(ResourceReference.class);
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);
        RelativeResourceResolver relativeResourceResolver = mock(RelativeResourceResolver.class);

        relativeResourceResolvers.add(relativeResourceResolver);
        when(source.getType()).thenReturn("blah");
        when(reference.getType()).thenReturn("any");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);
        when(relativeResourceResolver.resolve(source, reference)).thenReturn(Optional.<ResourceReference>absent());

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(reference, result);
    }

    @Test
    public void resolveRelativeDistinctTypes() throws Exception {
        ResourceReference expected = mock(ResourceReference.class);
        ResourceReference source = mock(ResourceReference.class);
        ResourceReference reference = mock(ResourceReference.class);
        RelativeResourceResolver relativeResourceResolver = mock(RelativeResourceResolver.class);

        relativeResourceResolvers.add(relativeResourceResolver);
        when(source.getType()).thenReturn("blah");
        when(reference.getType()).thenReturn("tu");
        when(resourceReferenceExtractor.extract("path")).thenReturn(reference);
        when(relativeResourceResolver.resolve(source, reference)).thenReturn(Optional.<ResourceReference>absent());

        ResourceReference result = underTest.resolve(source, "path");

        assertSame(reference, result);
    }

    @Test(expected = ResourceException.class)
    public void loadMetadataNoLoader() throws Exception {
        underTest.loadMetadata(new ResourceReference("haha", "path"));
    }
}