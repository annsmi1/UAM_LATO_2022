package pl.psi.artifacts;

import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Artifact implements ArtifactIf {

  private final ArtifactRank rank;

  private final ArtifactPlacement placement;

  private final String name;

  private final String description;

  private final double price;

  private final Set<ArtifactEffect<ArtifactEffectApplicable>> effects;

  public void applyTo(final ArtifactEffectApplicable aArtifactEffectApplicable) {
    effects.forEach(aArtifactEffectApplicable::applyArtifactEffect);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Artifact)) {
      return false;
    }
    final Artifact artifact = (Artifact) o;
    return Double.compare(artifact.getPrice(), getPrice()) == 0
        && getRank() == artifact.getRank() && getPlacement() == artifact.getPlacement()
        && getName().equals(artifact.getName()) && getDescription().equals(
        artifact.getDescription())
        && Objects.equals(getEffects(), artifact.getEffects());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getRank(), getPlacement(), getName(), getDescription(), getPrice(),
        getEffects());
  }
}
